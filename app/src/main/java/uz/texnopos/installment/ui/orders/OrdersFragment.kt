package uz.texnopos.installment.ui.orders

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.base.BaseFragment
import uz.texnopos.installment.core.Constants.TAG
import uz.texnopos.installment.core.toast
import uz.texnopos.installment.data.LoadingState
import uz.texnopos.installment.databinding.FragmentOrdersBinding


//Created by Alisher 07.09.21
class OrdersFragment : BaseFragment(R.layout.fragment_orders) {
    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.to_bottom_anim
        )
    }

    private val viewModel by viewModel<OrderViewModel>()
    private val adapter = OrderAdapter()
    private var clicked = false
    private lateinit var navController: NavController
    private lateinit var bind: FragmentOrdersBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentOrdersBinding.bind(view)
        navController = Navigation.findNavController(view)
        setStatusBarColor()

        bind.apply {
            rvClients.adapter=adapter
            adapter.onItemClick {
                navController.navigate(R.id.action_clientsFragment_to_fragmentClient)
            }
            floatingButton.setOnClickListener {
                onFloatingClicked()
            }
            floatingAddButton.setOnClickListener {
                navController.navigate(R.id.action_clientsFragment_to_fragmentAddClient)
            }
            floatingCalcButton.setOnClickListener {
                viewModel.getAllOrders()
                Toast.makeText(requireContext(), "Click", Toast.LENGTH_SHORT).show()
            }
            viewModel.orders.observe(requireActivity(), {
                when (it.status) {
                    LoadingState.LOADING -> showProgress()
                    LoadingState.SUCCESS -> {
                        hideProgress()
                        Log.d(TAG, "onViewCreated: ${it.data}")
                    }
                    LoadingState.ERROR -> {
                        hideProgress()
                        toast(it.message!!)
                    }
                }
            })
        }
    }

    private fun onFloatingClicked() {
        setAnimation(clicked)
        setVisibility(clicked)
        clicked=!clicked
    }

    private fun setVisibility(clicked: Boolean) {
        bind.floatingAddButton.isInvisible = clicked
        bind.floatingCalcButton.isInvisible = clicked
        if (!clicked) bind.floatingButton.alpha = 1f
        else bind.floatingButton.alpha = 0.8f
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            bind.floatingAddButton.startAnimation(fromBottom)
            bind.floatingCalcButton.startAnimation(fromBottom)
            bind.floatingButton.startAnimation(rotateOpen)
        } else {
            bind.floatingAddButton.startAnimation(toBottom)
            bind.floatingCalcButton.startAnimation(toBottom)
            bind.floatingButton.startAnimation(rotateClose)
        }
    }

    private fun setStatusBarColor() {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(),
            R.color.clientsFragmentStatusBarColor)
    }

    override fun onStart() {
        super.onStart()
        requireActivity().window.statusBarColor=ContextCompat.getColor(requireContext(),R.color.background_color)
    }
}
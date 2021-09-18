package uz.texnopos.installment.clients

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import uz.texnopos.installment.databinding.FragmentClientsBinding
import uz.texnopos.installment.R


//Created by Alisher 07.09.21
class ClientsFragment : Fragment(R.layout.fragment_clients) {
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
    private val adapter= ClientAdapter()
    private var clicked = false
    private lateinit var navController: NavController
    private lateinit var bind: FragmentClientsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentClientsBinding.bind(view)
        navController=Navigation.findNavController(view)
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
                navController.navigate(R.id.action_clientsFragment_to_fragmentPayment)
                Toast.makeText(requireContext(), "Click", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onFloatingClicked() {
        setAnimation(clicked)
        setVisibility(clicked)
        clicked=!clicked
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            bind.floatingAddButton.visibility=View.VISIBLE
            bind.floatingCalcButton.visibility=View.VISIBLE
            bind.floatingButton.alpha=1f
        } else {
            bind.floatingAddButton.visibility=View.INVISIBLE
            bind.floatingCalcButton.visibility=View.INVISIBLE
            bind.floatingButton.alpha=0.8f
        }
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
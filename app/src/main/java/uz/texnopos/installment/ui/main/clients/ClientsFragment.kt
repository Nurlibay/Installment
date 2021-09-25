package uz.texnopos.installment.ui.main.clients

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.core.BaseFragment
import uz.texnopos.installment.core.ResourceState
import uz.texnopos.installment.core.toast
import uz.texnopos.installment.databinding.FragmentClientsBinding

//Created by Alisher 07.09.21
class ClientsFragment : BaseFragment(R.layout.fragment_clients) {

    //private val viewModel by viewModel<ClientsViewModel>()
    private val adapter = ClientsAdapter()
    private lateinit var navController: NavController
    private lateinit var bind: FragmentClientsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentClientsBinding.bind(view)
        navController = Navigation.findNavController(view)

        bind.apply {
            rvClients.adapter = adapter
            adapter.onItemClick {
                navController.navigate(R.id.action_clientsFragment_to_clientFragment)
            }
            floatingButton.setOnClickListener {

            }
            /*viewModel.orders.observe(requireActivity(), {
                when (it.status) {
                    ResourceState.LOADING -> showProgress()
                    ResourceState.SUCCESS -> {
                        hideProgress()
                    }
                    ResourceState.ERROR -> {
                        hideProgress()
                        toast(it.message!!)
                    }
                }
            })*/
        }
    }

    override fun onStart() {
        super.onStart()
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.background_color)
    }
}
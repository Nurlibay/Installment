package uz.texnopos.installment.ui.main.clients

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.core.*
import uz.texnopos.installment.databinding.FragmentClientsBinding
import uz.texnopos.installment.settings.Settings.Companion.NO_INTERNET

class ClientsFragment : Fragment(R.layout.fragment_clients) {

    private val viewModel: ClientsViewModel by viewModel()
    private val adapter = ClientsAdapter()
    private lateinit var navController: NavController
    private lateinit var binding: FragmentClientsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClientsBinding.bind(view)
        navController = Navigation.findNavController(view)
        viewModel.getAllOrders()
        setStatusBarColor(R.color.background_color)
        binding.apply {
            rvClients.adapter = adapter
            adapter.onItemClick {
                try {
                    navController.navigate(R.id.action_clientsFragment_to_clientFragment)
                } catch (e: Exception) {
                }
            }
            floatingButton.setOnClickListener {

            }
            viewModel.clients.observe(requireActivity(), {
                when (it.status) {
                    ResourceState.LOADING -> showProgress()
                    ResourceState.SUCCESS -> {
                        adapter.models = it.data!!.toMutableList()
                        hideProgress()
                    }
                    ResourceState.ERROR -> {
                        hideProgress()
                        toast(it.message!!)
                    }
                    ResourceState.NETWORK_ERROR -> {
                        hideProgress()
                        toast(NO_INTERNET)
                    }
                }
            })
        }
    }
}
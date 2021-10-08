package uz.texnopos.installment.ui.main.clients

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.core.*
import uz.texnopos.installment.data.model.Client
import uz.texnopos.installment.databinding.FragmentClientsBinding
import uz.texnopos.installment.settings.Settings.Companion.CLIENT
import uz.texnopos.installment.settings.Settings.Companion.NO_INTERNET
import uz.texnopos.installment.settings.Settings.Companion.TAG

class ClientsFragment : Fragment(R.layout.fragment_clients) {

    private val viewModel: ClientsViewModel by viewModel()
    private val adapter = ClientsAdapter()
    private lateinit var navController: NavController
    private lateinit var binding: FragmentClientsBinding
    private  var clients: List<Client>?=null

    override fun onStart() {
        super.onStart()
        showProgress()
        refresh()
        setUpObserver()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClientsBinding.bind(view)
        navController = Navigation.findNavController(view)
        setStatusBarColor(R.color.background_color)
        binding.apply {
            container.setOnRefreshListener {
                if (binding.etSearch.checkIsEmpty()) {
                    refresh()
                } else {
                    binding.container.isRefreshing = false
                }
            }
            rvClients.adapter = adapter

            adapter.onItemClick {
                val bundle = Bundle()
                bundle.putParcelable(CLIENT, it)
                try {
                    navController.navigate(R.id.action_clientsFragment_to_clientFragment, bundle)
                } catch (e: Exception) { }
            }
            floatingButton.setOnClickListener {

            }

        }

        binding.etSearch.addTextChangedListener {

        }
    }

    private fun refresh() {
        viewModel.getAllClients()
    }

    private fun setUpObserver() {
        viewModel.clients.observe(requireActivity(), {
            when (it.status) {
                ResourceState.LOADING -> {
                }
                ResourceState.SUCCESS -> {
                    clients = it.data!!

                    hideProgress()
                    binding.container.isRefreshing = false
                }
                ResourceState.ERROR -> {
                    hideProgress()
                    toast(it.message!!)
                    binding.container.isRefreshing = false
                }
                ResourceState.NETWORK_ERROR -> {
                    hideProgress()
                    toast(NO_INTERNET)
                    binding.container.isRefreshing = false
                }
            }
        })
    }



}
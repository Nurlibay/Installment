package uz.texnopos.installment.ui.main.clients

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
    private lateinit var clients: List<Client>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpObserver()
    }
    override fun onStart() {
        super.onStart()
        showProgress()
        refresh()
<<<<<<< HEAD
        setUpObserver()
        Toast.makeText(requireContext(), "Fragment started !", Toast.LENGTH_SHORT).show()
=======

>>>>>>> 25ce06d23f46f4ecb1bc9cbc44a348bf6ee0cd84
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClientsBinding.bind(view)
        navController = Navigation.findNavController(view)
        setStatusBarColor(R.color.background_color)
        Toast.makeText(requireContext(), "Fragment views created !", Toast.LENGTH_SHORT).show()
        binding.apply {
            swipeRefresh.setOnRefreshListener {
                if (binding.etSearch.checkIsEmpty()) {
                    refresh()
                } else {
                    binding.swipeRefresh.isRefreshing = false
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
            filterClientNameAndClientId(it.toString())
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
                    adapter.models = it.data.toMutableList()
                    hideProgress()
                    binding.swipeRefresh.isRefreshing = false
                }
                ResourceState.ERROR -> {
                    hideProgress()
                    toast(it.message!!)
                    binding.swipeRefresh.isRefreshing = false
                }
                ResourceState.NETWORK_ERROR -> {
                    hideProgress()
                    toast(NO_INTERNET)
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        })
    }

    private fun filterClientNameAndClientId(s: String) {
        val filteredList: MutableList<Client> = mutableListOf()
        for (client in clients) {
            if (client.client_name.lowercase().contains(s.lowercase()) ||
                client.client_id.toString().lowercase().contains(s.lowercase())
<<<<<<< HEAD
            ) {
                filteredList.add(client)
            }
        }
        adapter.models = filteredList
=======
            ) clientsItem.add(client)
        }
        adapter.models = clientsItem
>>>>>>> 25ce06d23f46f4ecb1bc9cbc44a348bf6ee0cd84
    }
}
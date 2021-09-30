package uz.texnopos.installment.ui.main.clients

import android.os.Bundle
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

class ClientsFragment : Fragment(R.layout.fragment_clients) {

    private val viewModel: ClientsViewModel by viewModel()
    private val adapter = ClientsAdapter()
    private lateinit var navController: NavController
    private lateinit var binding: FragmentClientsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAllClients()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClientsBinding.bind(view)
        navController = Navigation.findNavController(view)
        setStatusBarColor(R.color.background_color)
        binding.apply {
            rvClients.adapter = adapter

            adapter.onItemClick {
                val bundle=Bundle()
                bundle.putParcelable(CLIENT,it)
                try {
                    navController.navigate(R.id.action_clientsFragment_to_clientFragment,bundle)
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

        binding.etSearch.addTextChangedListener {
            filter(it.toString())
        }
    }

    private fun filter(s: String){
        val clientsItem : MutableList<Client> = mutableListOf()
        viewModel.clients.observe(viewLifecycleOwner, {
            when(it.status){
                ResourceState.LOADING ->{
                    showProgress()
                }
                ResourceState.SUCCESS ->{
                    for(client in it.data!!.toMutableList()){
                        if(client.client_name.lowercase().contains(s.lowercase())){
                            clientsItem.add(client)
                        }
                    }
                    adapter.filteredList(clientsItem)
                    hideProgress()
                }
                ResourceState.ERROR ->{
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
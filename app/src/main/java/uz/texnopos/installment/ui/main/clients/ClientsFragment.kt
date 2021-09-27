package uz.texnopos.installment.ui.main.clients

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.core.BaseFragment
import uz.texnopos.installment.databinding.FragmentClientsBinding

class ClientsFragment : BaseFragment(R.layout.fragment_clients) {

    private val viewModel: ClientsViewModel by viewModel()
    private val adapter = ClientsAdapter()
    private lateinit var navController: NavController
    private lateinit var binding: FragmentClientsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClientsBinding.bind(view)
        navController = Navigation.findNavController(view)

        binding.apply {
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
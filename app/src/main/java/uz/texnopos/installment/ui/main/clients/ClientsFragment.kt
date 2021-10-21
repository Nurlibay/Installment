package uz.texnopos.installment.ui.main.clients

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
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
import uz.texnopos.installment.settings.Settings.Companion.TOKEN
import uz.texnopos.installment.ui.main.clients.calc.CalculatorDialog

class ClientsFragment : Fragment(R.layout.fragment_clients) {

    private val viewModel: ClientsViewModel by viewModel()
    private val adapter = ClientsAdapter()
    private lateinit var navController: NavController
    private lateinit var binding: FragmentClientsBinding
    private var clients: List<Client> = emptyList()

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
                } catch (e: Exception) {
                }
            }

            floatingButton.setOnClickListener {
                calcCustomDialog(it)
            }

            logout.setOnClickListener {
                deleteCache(requireContext())
                navController.navigate(R.id.action_clientsFragment_to_loginFragment)
                getSharedPreferences().removeKey(TOKEN)
            }
        }

        binding.etSearch.addTextChangedListener {
            if (adapter.filterClientNameAndClientId(it.toString(), clients)) {
                adapter.filterClientNameAndClientId(it.toString(), clients)
            } else {
                val toast = Toast.makeText(requireContext(), "Клиент не найден", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
        }

//        binding.popupMenuItemSort.setOnClickListener {
//            showPopup(it)
//        }
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
                    adapter.filterClientNameAndClientId(binding.etSearch.textToString(), clients)
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

//    private fun showPopup(view: View) {
//        val popup = PopupMenu(requireContext(), view)
//        popup.inflate(R.menu.menu_client)
//
//        popup.setOnMenuItemClickListener { item: MenuItem? ->
//
//            when (item!!.itemId) {
//                R.id.sortRed -> {
//
//                }
//                R.id.sortYellow -> {
//
//                }
//                R.id.sortGreen -> {
//
//                }
//            }
//            true
//        }
//        popup.show()
//    }

    private fun calcCustomDialog(view: View) {
        CalculatorDialog().show(requireActivity().supportFragmentManager, "This is custom dialog")
    }

}

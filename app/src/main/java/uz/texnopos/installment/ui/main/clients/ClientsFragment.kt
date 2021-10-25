package uz.texnopos.installment.ui.main.clients

import android.Manifest.permission.CALL_PHONE
import android.Manifest.permission.SEND_SMS
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.background.util.askPermission
import uz.texnopos.installment.background.util.isHasPermission
import uz.texnopos.installment.core.*
import uz.texnopos.installment.data.model.Client
import uz.texnopos.installment.databinding.FragmentClientsBinding
import uz.texnopos.installment.settings.Constants.ASK_SMS_PERMISSION_REQUEST_CODE
import uz.texnopos.installment.settings.Constants.CLIENT
import uz.texnopos.installment.settings.Constants.NO_INTERNET
import uz.texnopos.installment.settings.Constants.TOKEN
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
        setHasOptionsMenu(true)
        setStatusBarColor(R.color.background_color)


        if (!isHasPermission(SEND_SMS) ||
            !isHasPermission(CALL_PHONE)
        ) {
            askPermission(
                arrayOf(SEND_SMS,
                    CALL_PHONE),
                ASK_SMS_PERMISSION_REQUEST_CODE
            )
        }
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

            floatingButton.onClick {
                calcCustomDialog()
            }

            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.itemLogout -> {
                        AlertDialog.Builder(requireContext(), R.style.LogoutAlertDialogTheme)
                            .apply {
                                setTitle(getString(R.string.logout_title))
                                setMessage(getString(R.string.supporting_text))
                                setPositiveButton("Выйти") { _, _ ->
                                    logOut()
                                }
                                setNeutralButton("Отмена") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                create()
                                show()
                            }
                        true
                    }
                    R.id.itemSort -> {
                        showPopup(toolbar.findViewById(R.id.itemLogout))
                        true
                    }
                    else -> false

                }

            }
            etSearch.addTextChangedListener {
                if (adapter.filterClientNameAndClientId(it.toString(), clients)) {
                    adapter.filterClientNameAndClientId(it.toString(), clients)
                }
                clientNotFound.isVisible = adapter.models.isEmpty()
            }
        }
    }

    private fun logOut() {
        deleteCache(requireContext())
        navController.navigate(R.id.action_clientsFragment_to_loginFragment)
        getSharedPreferences().removeKey(TOKEN)
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

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray,
    ) {
        if (requestCode == ASK_SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                askPermission(
                    arrayOf(SEND_SMS,
                        CALL_PHONE),
                    ASK_SMS_PERMISSION_REQUEST_CODE
                )
        }
    }

    private fun calcCustomDialog() {
        CalculatorDialog().show(requireActivity().supportFragmentManager, "This is custom dialog")
    }


    @SuppressLint("RestrictedApi")
    private fun showPopup(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.inflate(R.menu.item_menu_sort)
        popup.gravity=Gravity.CENTER
        val menuHelper = MenuPopupHelper(requireContext(),
            (popup.menu as MenuBuilder?)!!, view)
        menuHelper.setForceShowIcon(true)
        popup.setOnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.sortRed -> {
                adapter.sortByColor("red",clients)
                }
                R.id.sortYellow -> {
                    adapter.sortByColor("yellow",clients)
                }
                R.id.sortGreen -> {
                    adapter.sortByColor("green",clients)
                }
            }
            true
        }
        popup.show()
        menuHelper.show()
    }
}

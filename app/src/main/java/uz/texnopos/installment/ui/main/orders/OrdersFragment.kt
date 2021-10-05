package uz.texnopos.installment.ui.main.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import uz.texnopos.installment.R
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.core.*
import uz.texnopos.installment.data.model.Client
import uz.texnopos.installment.databinding.FragmentOrdersBinding
import uz.texnopos.installment.settings.Settings.Companion.CLIENT
import uz.texnopos.installment.settings.Settings.Companion.NO_INTERNET
import uz.texnopos.installment.settings.Settings.Companion.ORDER

class OrdersFragment : Fragment(R.layout.fragment_orders) {

    private lateinit var binding: FragmentOrdersBinding
    private lateinit var navController: NavController
    private val viewModel: OrdersViewModel by viewModel()
    private val adapter = OrdersAdapter()
    private var client: Client? = null

    companion object {
        const val REQUEST_CALL = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showProgress()
        client = arguments?.getParcelable(CLIENT)
        setUpObservers()
        refresh()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.background_blue)
        navController = Navigation.findNavController(view)
        binding = FragmentOrdersBinding.bind(view)
        binding.tvClientName.text = client!!.client_name
        binding.tvClientPhone.text = client!!.phone1
        binding.swipeRefresh.setOnRefreshListener {
            refresh()
        }
        adapter.onItemClick {
            val bundle = Bundle()
            bundle.putParcelable(CLIENT, client)
            bundle.putParcelable(ORDER, it)
            try {
                navController.navigate(R.id.action_clientFragment_to_clientTransactionsFragment, bundle)
            } catch (e: Exception) {}
        }
        binding.rvOrders.adapter = adapter
        binding.btnFab.setOnClickListener {
            makePhoneCall()
        }
    }

    private fun makePhoneCall() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.CALL_PHONE), REQUEST_CALL
            )
        } else {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:${binding.tvClientPhone.text}")
            startActivity(callIntent)
        }
    }

    private fun refresh() {
        viewModel.getOrders(client!!.client_id)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall()
            } else {
                Toast.makeText(requireContext(), "PERMISSION DENIED", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setUpObservers() {
        viewModel.orders.observe(requireActivity()) {
            when (it.status) {
                ResourceState.LOADING -> {
                }
                ResourceState.SUCCESS -> {
                    hideProgress()
                    adapter.setData(it.data!!)
                    binding.swipeRefresh.isRefreshing = false
                }
                ResourceState.ERROR -> {
                    toast(it.message!!)
                    hideProgress()
                    binding.swipeRefresh.isRefreshing = false
                }
                ResourceState.NETWORK_ERROR -> {
                    hideProgress()
                    toast(NO_INTERNET)
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }
    }
}
package uz.texnopos.installment.ui.main.transactions

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
import uz.texnopos.installment.core.ResourceState
import uz.texnopos.installment.core.toast
import uz.texnopos.installment.core.*
import uz.texnopos.installment.databinding.FragmentTransactionsBinding
import uz.texnopos.installment.settings.Settings

class ClientTransactionsFragment : Fragment(R.layout.fragment_transactions) {

    private val adapter = ClientTransactionsAdapter()
    private lateinit var binding: FragmentTransactionsBinding
    private val viewModel: ClientTransactionsViewModel by viewModel()
    private lateinit var navController: NavController
    var orderId:Int?=null
    companion object {
        const val REQUEST_CALL = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderId=arguments?.getInt("orderId")!!
        viewModel.getTransactions(orderId!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTransactionsBinding.bind(view)
        navController = Navigation.findNavController(view)
        setStatusBarColor(R.color.background_blue)
        binding.rvOrders.adapter = adapter

        binding.floatingActionButton.setOnClickListener {
            val bundle=Bundle()
            bundle.putInt("orderId",orderId!!)
            navController.navigate(R.id.action_clientTransactionsFragment_to_paymentFragment,bundle)
        }

        setUpObservers()
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

    override fun onStart() {
        super.onStart()
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.item_background)
    }

    private fun setUpObservers() {
        viewModel.transactions.observe(requireActivity()) {
            when (it.status) {
                ResourceState.LOADING -> {
                    showProgress()
                }
                ResourceState.SUCCESS -> {
                    hideProgress()
                    adapter.models=it.data!!.toMutableList()
                }
                ResourceState.ERROR -> {
                    toast(it.message!!)
                    hideProgress()
                }
                ResourceState.NETWORK_ERROR -> {
                    hideProgress()
                    toast(Settings.NO_INTERNET)
                }
            }
        }
    }
}
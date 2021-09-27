package uz.texnopos.installment.ui.main.client.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import uz.texnopos.installment.R
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.core.*
import uz.texnopos.installment.databinding.FragmentClientOrdersBinding
import uz.texnopos.installment.settings.Settings

class ClientOrdersFragment : Fragment(R.layout.fragment_client_orders) {

    private lateinit var binding: FragmentClientOrdersBinding
    private lateinit var navController: NavController
    private val viewModel: ClientOrdersViewModel by viewModel()
    private val adapter = ClientOrdersAdapter()

    companion object {
        const val REQUEST_CALL = 1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.background_blue)
        binding = FragmentClientOrdersBinding.bind(view)
        navController = Navigation.findNavController(view)
        adapter.onItemClick {
            navController.navigate(R.id.action_clientFragment_to_clientTransactionsFragment)

        }
        binding.rvOrders.adapter = adapter
        setUpObservers()
    }

    private fun makePhoneCall() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
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
        if(requestCode == REQUEST_CALL){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makePhoneCall()
            } else {
                Toast.makeText(requireContext(), "PERMISSION DENIED", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setUpObservers() {
        viewModel.orders.observe(viewLifecycleOwner) {
            when(it.status) {
                ResourceState.LOADING -> showProgress()
                ResourceState.SUCCESS -> {

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
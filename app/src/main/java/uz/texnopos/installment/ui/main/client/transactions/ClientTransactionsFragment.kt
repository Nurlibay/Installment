package uz.texnopos.installment.ui.main.client.transactions

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
import uz.texnopos.installment.core.BaseFragment
import uz.texnopos.installment.databinding.FragmentClientTransactionsBinding

class ClientTransactionsFragment : BaseFragment(R.layout.fragment_client_transactions) {

    private lateinit var binding: FragmentClientTransactionsBinding
    private lateinit var navController: NavController

    companion object {
        const val REQUEST_CALL = 1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClientTransactionsBinding.bind(view)
        navController = Navigation.findNavController(view)

        setStatusBarColor()
        binding.floatingActionButton.setOnClickListener {
            navController.navigate(R.id.action_clientTransactionsFragment_to_paymentFragment)
            //makePhoneCall()
        }

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

    private fun setStatusBarColor() {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity?.window?.statusBarColor = ContextCompat.getColor(
            requireContext(),
            R.color.clientFragmentStatusBarColor
        )
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
    override fun onStart() {
        super.onStart()
        requireActivity().window.statusBarColor= ContextCompat.getColor(requireContext(),R.color.item_background)
    }
}
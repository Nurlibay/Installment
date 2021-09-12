package uz.texnopos.installment.ui.client

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import uz.texnopos.installment.R
import uz.texnopos.installment.ui.client.pager.ViewPagerAdapter
import uz.texnopos.installment.databinding.FragmentClientBinding
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class FragmentClient : Fragment(R.layout.fragment_client) {

    private lateinit var binding: FragmentClientBinding

    companion object {
        const val REQUEST_CALL = 1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClientBinding.bind(view)
        val adapter = ViewPagerAdapter(this)
        binding.pager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "пред месяц"
                }
                1 -> {
                    tab.text = "текущии месяц"
                }
                2 -> {
                    tab.text = "след месяц"
                }
            }
        }.attach()

        setStatusBarColor()
        binding.floatingActionButton.setOnClickListener {
            makePhoneCall()
        }

    }

    private fun makePhoneCall() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(android.Manifest.permission.CALL_PHONE), REQUEST_CALL)
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
}
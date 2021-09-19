package uz.texnopos.installment.ui.payment

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import uz.texnopos.installment.R
import uz.texnopos.installment.databinding.FragmentPaymentBinding

class FragmentPayment: Fragment(R.layout.fragment_payment) {

    private lateinit var binding: FragmentPaymentBinding
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPaymentBinding.bind(view)
        navController = Navigation.findNavController(view)
        setStatusBarColor()
    }

    private fun setStatusBarColor() {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(),
            R.color.paymentFragmentStatusBarColor)
    }
}
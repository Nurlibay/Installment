package uz.texnopos.installment.ui.payment

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import uz.texnopos.installment.R

class PaymentFragment: Fragment(R.layout.fragment_payment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        requireActivity().window.statusBarColor= ContextCompat.getColor(requireContext(),R.color.paymentFragmentStatusBarColor)
    }

}
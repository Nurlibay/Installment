package uz.texnopos.installment.ui.main.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.core.*
import uz.texnopos.installment.data.model.Payment
import uz.texnopos.installment.databinding.FragmentPaymentBinding
import uz.texnopos.installment.settings.Settings.Companion.NO_INTERNET
import kotlin.random.Random.Default.nextLong

class PaymentDialog : BottomSheetDialogFragment() {
    private var savedViewInstance: View? = null
    private lateinit var bind: FragmentPaymentBinding
    private val viewModel by viewModel<PaymentViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ThemeOverlay_Demo_BottomSheetDialog)
        return if (savedInstanceState != null) {
            savedViewInstance
        } else {
            savedViewInstance =
                inflater.inflate(R.layout.fragment_payment, container, true)
            savedViewInstance
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObserver()
        bind = FragmentPaymentBinding.bind(view).apply {
            btnPay.onClick {
                if (validate()) {
                    viewModel.payment(
                        Payment(nextLong(1, 10), etAddPayment.textToString().toLong())
                    )
                }
            }
        }
    }

    private fun validate(): Boolean {
        return if (bind.etAddPayment.checkIsEmpty()) {
            bind.etAddPayment.showError(getString(R.string.required_ru))
            false
        } else true
    }

    private fun setUpObserver() {
        viewModel.payment.observe(viewLifecycleOwner, {
            when (it.status) {
                ResourceState.LOADING -> showProgress()
                ResourceState.SUCCESS -> {
                    hideProgress()
                    toast(it.data as String)
                    dismiss()
                }
                ResourceState.ERROR -> {
                    hideProgress()
                    toast(it.message!!)
                }
                ResourceState.NETWORK_ERROR -> {
                    hideProgress()
                    toast(NO_INTERNET)
                }
            }
        })
    }
}
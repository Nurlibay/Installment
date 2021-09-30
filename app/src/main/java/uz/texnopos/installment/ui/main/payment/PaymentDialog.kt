package uz.texnopos.installment.ui.main.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnNextLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.core.*
import uz.texnopos.installment.data.model.Payment
import uz.texnopos.installment.databinding.FragmentPaymentBinding
import uz.texnopos.installment.settings.Settings.Companion.NO_INTERNET
import uz.texnopos.installment.ui.main.transactions.TransactionsFragment

class PaymentDialog(private val mFragment: TransactionsFragment) : BottomSheetDialogFragment() {
    private var savedViewInstance: View? = null
    lateinit var bind: FragmentPaymentBinding
    private val viewModel by viewModel<PaymentViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpObserver()
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ThemeOverlay_Demo_BottomSheetDialog)
        return if (savedInstanceState != null) {
            savedViewInstance
        } else {
            savedViewInstance =
                inflater.inflate(R.layout.fragment_payment, container, true)
            savedViewInstance
        }
    }

    init {
        show(mFragment.requireActivity().supportFragmentManager, "tag")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentPaymentBinding.bind(view).apply {
            countProduct.text = "Всего товаров: ${mFragment.client!!.count}"
            tvDebtValue.text = mFragment.client!!.all_sum.toInt().toString().changeFormat()

            etAddPayment.addTextChangedListener {
                inputPayment.helperText = it.toString().changeFormat()
            }

            btnPay.onClick {
                if (validate()) {
                    viewModel.payment(
                        Payment(mFragment.order!!.order_id, etAddPayment.textToString().toLong())
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
        viewModel.payment.observe(requireActivity(), {
            when (it.status) {
                ResourceState.LOADING -> showProgress()
                ResourceState.SUCCESS -> {
                    toast(it.data as String)
                    mFragment.refresh()
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
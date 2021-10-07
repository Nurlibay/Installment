package uz.texnopos.installment.ui.main.payment

import android.os.Bundle
import android.util.Log
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
import uz.texnopos.installment.settings.Settings.Companion.TAG
import uz.texnopos.installment.ui.main.transactions.TransactionsFragment

class PaymentDialog(private val mFragment: TransactionsFragment) : BottomSheetDialogFragment() {
    private var savedViewInstance: View? = null
    lateinit var bind: FragmentPaymentBinding
    private val viewModel by viewModel<PaymentViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ThemeOverlay_Demo_BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: ")
        setUpObserver()
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
            val transactions = mFragment.transaction.value!!
            tvCurrentDebtValue.text = transactions.amount.changeFormat()
            tvDebtValue.text = transactions.all_debt.toInt().toString().changeFormat()
            etAddPayment.addTextChangedListener(MaskWatcherPrice(etAddPayment))
            btnPay.onClick {
                val inputSum = etAddPayment.textToString().getOnlyDigits().toLong()
                val allDebt = transactions.all_debt.toLong()
                if (validate()) {
                    if (inputSum <= allDebt)
                        viewModel.payment(Payment(mFragment.order!!.order_id, inputSum))
                    else toast("Неверная сумма!")
                }
            }
        }
    }
    private fun String.getOnlyDigits():String{
        var s=""
        this.forEach { if (it.isDigit()) s+=it }
        return s
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
                    hideProgress()
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
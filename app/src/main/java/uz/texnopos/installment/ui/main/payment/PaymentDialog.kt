package uz.texnopos.installment.ui.main.payment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
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
import kotlin.math.ceil

class PaymentDialog(private val mFragment: TransactionsFragment) : BottomSheetDialogFragment() {
    private var savedViewInstance: View? = null
    lateinit var bind: FragmentPaymentBinding
    private val viewModel by viewModel<PaymentViewModel>()
    var k=false

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
            val transaction = mFragment.transaction.value!!
            payItOfAll.isVisible=transaction.unprotsent_sum!=0.0
            tvCurrentDebtValue.text =transaction.amount.toInt().toString().changeFormat()
            tvDebtValue.text = transaction.all_debt.toInt().toString().changeFormat()
            etAddPayment.addTextChangedListener(MaskWatcherPrice(etAddPayment))

            tvCurrentDebtValue.onClick {
                etAddPayment.setText(this.textToString())
                k=true
            }

            payItOfAll.onClick {
                payItAllOff(transaction.unprotsent_sum)
            }
            btnPay.onClick {
                val allDebt = transaction.all_debt.toLong()
                if (validate()) {
                    val inputSum = etAddPayment.textToString().getOnlyDigits().toDouble()
                    if (inputSum <= allDebt){
                        if (inputSum.toInt()==transaction.amount.toInt()){
                            viewModel.payment(Payment(mFragment.order!!.order_id, transaction.amount))
                        }
                        else viewModel.payment(Payment(mFragment.order!!.order_id, inputSum))

                    }
                    else toast("Неверная сумма!")
                }
            }
        }
    }

    private fun String.getOnlyDigits(): String {
        var s = ""
        this.forEach { if (it.isDigit()) s += it }
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

    private fun payItAllOff(quantity: Double) {
        AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme).apply {
            setTitle(getString(R.string.confirm_payment))
            setMessage("Погасить весь имеющийся долг. Расчетная сумма ${
                quantity.toInt().toString().changeFormat()
            }ов")
            setPositiveButton("Платить") { _, _ ->
                viewModel.payment(Payment(mFragment.order!!.order_id, quantity))
            }
            setNeutralButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }
}
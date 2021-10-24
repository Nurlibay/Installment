package uz.texnopos.installment.ui.main.payment

import android.os.Bundle
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
import uz.texnopos.installment.settings.Constants.NO_INTERNET
import uz.texnopos.installment.ui.main.transactions.TransactionsFragment

class PaymentDialog(private val mFragment: TransactionsFragment) : BottomSheetDialogFragment() {
    private var savedViewInstance: View? = null
    lateinit var bind: FragmentPaymentBinding
    private val viewModel by viewModel<PaymentViewModel>()
    private val order=mFragment.order!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ThemeOverlay_Demo_BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        setUpObserver()
        return if (savedInstanceState != null) {
            savedViewInstance
        } else {
            savedViewInstance = inflater.inflate(R.layout.fragment_payment, container, true)
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
            val amount=if (transaction.status!=1) transaction.amount.changeFormat()
            else {
                tvCurrentDebt.isVisible=false
                "В этом месяце\nнет долга"
            }
            payItOfAll.isVisible=transaction.withoutRate!=0.0
            tvDebtValue.text=transaction.allDebt.changeFormat()
            tvCurrentDebtValue.text =amount
            etAddPayment.addTextChangedListener(MaskWatcherPrice(etAddPayment))

            tvCurrentDebtValue.onClick {
                if (transaction.status!=1)
                etAddPayment.setText(this.textToString())
            }

            payItOfAll.onClick {
                payItAllOff(transaction.withoutRate)
            }

            btnPay.onClick {
                if (validate()) {
                    val inputSum = etAddPayment.textToString().getOnlyDigits().toDouble()
                    if (inputSum <= transaction.withoutRate){
                        if (inputSum.toInt() == transaction.amount.toInt())
                            viewModel.payment(Payment(order.orderId, transaction.amount))
                        else
                            viewModel.payment(Payment(order.orderId, inputSum))
                    }
                    else toast("Неверная сумма!")
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
        val q=quantity.changeFormat()
        AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme).apply {
            setTitle(getString(R.string.confirm_payment))
            setMessage(getString(R.string.request_pay,q))
            setPositiveButton("Платить") { _, _ ->
                viewModel.payment(Payment(mFragment.order!!.orderId, quantity))
            }
            setNeutralButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }
}
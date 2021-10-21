package uz.texnopos.installment.ui.main.clients.calc

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import uz.texnopos.installment.R
import uz.texnopos.installment.core.*
import uz.texnopos.installment.data.model.Amount
import uz.texnopos.installment.databinding.CustomDialogFragmentBinding

class CalculatorDialog : DialogFragment() {

    private lateinit var binding: CustomDialogFragmentBinding
    private val amount = MediatorLiveData<Amount>().apply { value = Amount() }
    var livePrice = MutableLiveData<Double>()
    var liveFirstPay = MutableLiveData<Double>()
    var livePercent = MutableLiveData<Int>()
    var liveMonth = MutableLiveData<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner);
        return inflater.inflate(R.layout.custom_dialog_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CustomDialogFragmentBinding.bind(view)

        binding.apply {
            etPrice.addTextChangedListener(MaskWatcherPrice(etPrice))
            etFirstPay.addTextChangedListener(MaskWatcherPrice(etFirstPay))
            etMonth.filters = arrayOf<InputFilter>(MinMaxFilter(1, 100))
            etProcent.filters = arrayOf<InputFilter>(MinMaxFilter(1, 100))
            merge()
            observe()
            etPrice.doOnTextChanged { it, _, _, _ ->
                if (it.isNullOrEmpty()) {
                    livePrice.postValue(0.0)
                } else {
                    livePrice.postValue(it.toString().getOnlyDigits().toDouble())
                }
            }
            etFirstPay.doOnTextChanged { it, _, _, _ ->
                if (it.isNullOrEmpty()) {
                    liveFirstPay.postValue(0.0)
                } else {
                    liveFirstPay.postValue(it.toString().getOnlyDigits().toDouble())
                }
            }
            etProcent.doAfterTextChanged {
                if (!it.isNullOrEmpty()) {
                    livePercent.postValue(it.toString().getOnlyDigits().toInt())
                } else {
                    livePercent.postValue(0)
                }
            }
            etMonth.doAfterTextChanged {
                if (!it.isNullOrEmpty()) {
                    if (it.toString().toInt() != 0) {
                        liveMonth.postValue(it.toString().getOnlyDigits().toInt())
                    }
                } else {
                    liveMonth.postValue(0)
                }
            }
        }
    }

    private fun merge() {
        amount.addSource(livePrice) {
            val previous = amount.value
            amount.value = previous?.copy(product_price = it)
        }
        amount.addSource(liveFirstPay) {
            val previous = amount.value
            amount.value = previous?.copy(first_pay = it)
        }
        amount.addSource(livePercent) {
            val previous = amount.value
            amount.value = previous?.copy(percent = it)
        }
        amount.addSource(liveMonth) {
            val previous = amount.value
            amount.value = previous?.copy(month = it)
        }
    }

    private fun observe() {
        amount.observe(requireActivity(), {
            val result = (((it.product_price - it.first_pay) *
                    ((it.percent * it.month).toDouble() / 100 + 1)) / it.month).toLong()
            if (validate() && it.product_price >= it.first_pay) {
                binding.tvResult.text = result.toString().changeFormat()
            } else if (it.product_price < it.first_pay) {
                binding.tvResult.text = "Неверная сумма!"
            } else {
                binding.tvResult.text = ""
            }
        })
    }

    private fun String.getOnlyDigits(): String {
        var s = ""
        this.forEach { if (it.isDigit()) s += it }
        return if (s == "") "0"
        else s
    }

    private fun validate(): Boolean {
        return (!binding.etPrice.checkIsEmpty()
                && !binding.etProcent.checkIsEmpty() && !binding.etMonth.checkIsEmpty())
    }
}
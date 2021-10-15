package uz.texnopos.installment.ui.main.clients.calc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.core.*
import uz.texnopos.installment.data.model.Amount
import uz.texnopos.installment.databinding.CustomDialogFragmentBinding

class CalculatorDialog : DialogFragment() {

    private lateinit var binding: CustomDialogFragmentBinding
    private val viewModel: CalcViewModel by viewModel()
    val amount = MediatorLiveData<Amount>().apply { value=Amount() }
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
            var price = 0.0
            var firstPay = 0.0
            var percent = 0
            var month = 0
            merge()
            observe()
            etPrice.doOnTextChanged { text, _, _, _ ->
                livePrice.postValue(text.toString().getOnlyDigits().toDouble())
            }
            etFirstPay.doOnTextChanged { text, start, before, count ->
                liveFirstPay.postValue(text.toString().getOnlyDigits().toDouble())
            }
            etProcent.doOnTextChanged { text, start, before, count ->
                livePercent.postValue(text.toString().getOnlyDigits().toInt())
            }
            etMonth.doOnTextChanged { text, start, before, count ->
                liveMonth.postValue(text.toString().getOnlyDigits().toInt())
            }
            if (price < firstPay) {
                toast("Неверная сумма!")
            } else {
//                tvResult.text = result.toLong().toString().changeFormat()
            }
        }
    }

    fun merge() {
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

    fun observe() {
        amount.observe(requireActivity(),{
            val result = (((it.product_price - it.first_pay) * ((it.percent * it.month).toDouble() / 100 + 1)) / it.month).toInt()
            if (validate()) binding.tvResult.text=result.toString().changeFormat()
            else binding.tvResult.text="Неверная сумма!"
        })
    }

    private fun String.getOnlyDigits(): String {
        var s = ""
        this.forEach { if (it.isDigit()) s += it }
        return s
    }

    private fun validate(): Boolean {
        return (!binding.etPrice.checkIsEmpty() && !binding.etFirstPay.checkIsEmpty()
                && !binding.etProcent.checkIsEmpty() && !binding.etMonth.checkIsEmpty())
    }
}
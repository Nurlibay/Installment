package uz.texnopos.installment.ui.main.clients.calc

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.core.*
import uz.texnopos.installment.databinding.CustomDialogFragmentBinding

class CalculatorDialog : DialogFragment() {

    private lateinit var binding: CustomDialogFragmentBinding
    private val viewModel: CalcViewModel by viewModel()

    var livePrice = MutableLiveData<Long>()
    var liveFirstPay = MutableLiveData<Long>()
    var livePercent = MutableLiveData<Long>()
    var liveMonth = MutableLiveData<Long>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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

            var price: Long = 0
            var firstPay: Long = 0
            var percent: Long = 0
            var month: Long = 0

            livePrice.value = etPrice.textToString().getOnlyDigits().toLong()
            livePrice.observe(requireActivity(), {
                price = it
            })

            liveFirstPay.value = etFirstPay.textToString().getOnlyDigits().toLong()
            liveFirstPay.observe(requireActivity(), {
                firstPay = it
            })

            livePercent.value = etProcent.textToString().getOnlyDigits().toLong()
            livePercent.observe(requireActivity(), {
                percent = it
            })

            liveMonth.value = etMonth.textToString().getOnlyDigits().toLong()
            liveMonth.observe(requireActivity(), {
                month = it
            })

            toast(price.toString())
            toast(firstPay.toString())
            toast(percent.toString())
            toast(month.toString())

            //val result = (((price - firstPay) * ((percent * month).toDouble() / 100 + 1)) / month).toInt()

            if (price < firstPay) {
                toast("Неверная сумма!")
            } else {
                //tvResult.text = result.toLong().toString().changeFormat()
            }
        }
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
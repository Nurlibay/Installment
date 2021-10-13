package uz.texnopos.installment.ui.main.clients.calc

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalcViewModel: ViewModel() {

    private val number : MutableLiveData<Int> = MutableLiveData()
    val num get() = number

    fun onFieldChangeValue(a: Int) {
        val temp = number.value
    }
}
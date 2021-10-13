package uz.texnopos.installment.ui.main.payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.texnopos.installment.core.Resource
import uz.texnopos.installment.core.isNetworkAvailable
import uz.texnopos.installment.data.model.Payment
import uz.texnopos.installment.data.retrofit.ApiInterface

class PaymentViewModel(private val api: ApiInterface) : ViewModel() {

    private var _payment: MutableLiveData<Resource<Any>> = MutableLiveData()
    val payment get() = _payment

//    fun payment(payment: Payment) {
//        _payment.value = Resource.loading()
//        if (isNetworkAvailable())
//            viewModelScope.launch {
//                load(payment)
//            }
//        else _payment.value = Resource.networkError()
//
//    }

    fun payment(payment: Payment) = viewModelScope.launch {
        _payment.value = Resource.loading()
        if (isNetworkAvailable()) {
            val response = api.payment(payment)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) _payment.value =
                    Resource.success(response.body()!!.message)
                else _payment.value = Resource.error(response.message())
            }
        } else _payment.value = Resource.networkError()
    }

//    private suspend fun load(payment: Payment) {
//        val response = api.payment(payment)
//        withContext(Dispatchers.Main) {
//            if (response.isSuccessful) {
//                _payment.value = Resource.success(response.body()!!.message)
//            } else {
//                _payment.value = Resource.error(response.message())
//            }
//        }
//    }
}

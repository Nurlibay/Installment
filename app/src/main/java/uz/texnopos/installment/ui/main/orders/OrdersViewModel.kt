package uz.texnopos.installment.ui.main.orders

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.texnopos.installment.core.Resource
import uz.texnopos.installment.core.isNetworkAvailable
import uz.texnopos.installment.data.model.Order
import uz.texnopos.installment.data.retrofit.ApiInterface
import java.net.UnknownHostException

class OrdersViewModel(private val api: ApiInterface) : ViewModel() {
    private var _orders: MutableLiveData<Resource<List<Order>>> = MutableLiveData()
    val orders get() = _orders


    fun getOrders(clientId: Int) = viewModelScope.launch {
        _orders.value = Resource.loading()
        if (isNetworkAvailable()) {
            try {
                val response = api.getOrders(clientId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val body = response.body()!!
                        if (body.successful) _orders.value =
                            Resource.success(body.payload)
                        else _orders.value = Resource.error(body.message)
                    } else _orders.value = Resource.error(response.message())
                }
            } catch (e: Exception) {
                if (e is UnknownHostException) _orders.value = Resource.networkError()
                else _orders.value = Resource.error(e.localizedMessage)
            }


        } else _orders.value = Resource.networkError()
    }
}

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

class OrdersViewModel(private val api: ApiInterface) : ViewModel() {
    private var _orders: MutableLiveData<Resource<List<Order>>> = MutableLiveData()
    val orders get() = _orders

    fun getOrders(clientId: Int) {
        _orders.value = Resource.loading()
        if (isNetworkAvailable())
            viewModelScope.launch {
                load(clientId)
            }
        else _orders.value = Resource.networkError()

    }

    private suspend fun load(clientId: Int) {
        val response = api.getOrders(clientId)
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                if (response.body()!!.successful)
                    _orders.value = Resource.success(response.body()!!.payload)
                else _orders.value = Resource.error(response.body()!!.message)
            } else _orders.value = Resource.error(response.message())
        }
    }
}
package uz.texnopos.installment.ui.orders

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.texnopos.installment.core.callApi
import uz.texnopos.installment.data.Resource
import uz.texnopos.installment.data.model.Client
import uz.texnopos.installment.data.model.Order
import uz.texnopos.installment.data.repository.Repository

class OrderViewModel(private val repo: Repository) : ViewModel() {
    private var _orders: MutableLiveData<Resource<List<Client>>> = MutableLiveData()
    val orders get() = _orders

     fun getAllOrders() {
         _orders.value = Resource.loading()

             callApi(repo.getAllClients(),
                 onApiSuccess = {
                     _orders.value = Resource.success(it!!.payload)
                 },
                 onApiError = {
                     _orders.value = Resource.error(it)
                 })
         }

}
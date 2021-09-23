package uz.texnopos.installment.ui.main.orders

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.installment.core.Resource
import uz.texnopos.installment.data.model.Client

class OrderViewModel() : ViewModel() {
    private var _orders: MutableLiveData<Resource<List<Client>>> = MutableLiveData()
    val orders get() = _orders

//     fun getAllOrders() {
//         _orders.value = Resource.loading()
//
//             callApi(repo.getAllClients(),
//                 onApiSuccess = {
//                     _orders.value = Resource.success(it!!.payload)
//                 },
//                 onApiError = {
//                     _orders.value = Resource.error(it)
//                 })
//         }

}
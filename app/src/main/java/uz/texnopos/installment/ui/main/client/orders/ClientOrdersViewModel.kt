package uz.texnopos.installment.ui.main.client.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import uz.texnopos.installment.core.Resource
import uz.texnopos.installment.data.model.Order
import uz.texnopos.installment.data.retrofit.ApiInterface
import uz.texnopos.installment.settings.Settings

class ClientOrdersViewModel(private val api: ApiInterface) : ViewModel() {
    private var mutableOrders : MutableLiveData<Resource<Order>> = MutableLiveData()
    val orders: LiveData<Resource<Order>> get() = mutableOrders

    //private val compositeDisposable = CompositeDisposable()

    fun getOrders() {
        mutableOrders.value = Resource.loading()

    }
}
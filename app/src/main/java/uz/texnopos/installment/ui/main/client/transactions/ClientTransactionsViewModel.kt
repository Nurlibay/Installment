package uz.texnopos.installment.ui.main.client.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.installment.core.Resource
import uz.texnopos.installment.data.model.Order
import uz.texnopos.installment.data.retrofit.ApiInterface

class ClientTransactionsViewModel(private val api: ApiInterface) : ViewModel() {
    private var mutableTransactions : MutableLiveData<Resource<Order>> = MutableLiveData()
    val transactions: LiveData<Resource<Order>> get() = mutableTransactions

    fun getTransactions() {
        mutableTransactions.value = Resource.loading()

    }
}
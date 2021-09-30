package uz.texnopos.installment.ui.main.transactions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.texnopos.installment.core.Resource
import uz.texnopos.installment.core.isNetworkAvailable
import uz.texnopos.installment.data.model.Transaction
import uz.texnopos.installment.data.retrofit.ApiInterface

class TransactionsViewModel(private val api: ApiInterface) : ViewModel() {
    private var _transactions : MutableLiveData<Resource<List<Transaction>>> = MutableLiveData()
    val transactions get() = _transactions

    fun getTransactions(orderId:Int) {
        _transactions.value = Resource.loading()
        if (isNetworkAvailable())
            viewModelScope.launch {
                load(orderId)
            }
        else _transactions.value= Resource.networkError()
    }

    private suspend fun load(orderId: Int){
        val response=api.getAllTransactions(orderId)
        withContext(Dispatchers.Main){
            if (response.isSuccessful){
                if (response.body()!!.successful) _transactions.value= Resource.success(response.body()!!.payload)
                else _transactions.value= Resource.error(response.body()!!.message)
            }
            else _transactions.value= Resource.error(response.message())
        }
    }
}
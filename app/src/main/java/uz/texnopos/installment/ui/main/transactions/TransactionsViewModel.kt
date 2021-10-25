package uz.texnopos.installment.ui.main.transactions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.texnopos.installment.core.Resource
import uz.texnopos.installment.core.isNetworkAvailable
import uz.texnopos.installment.data.model.Transactions
import uz.texnopos.installment.data.retrofit.ApiInterface
import java.net.UnknownHostException

class TransactionsViewModel(private val api: ApiInterface) : ViewModel() {
    private var _transactions: MutableLiveData<Resource<Transactions>> = MutableLiveData()
    val transactions get() = _transactions

    fun getTransactions(orderId: Int) = viewModelScope.launch {
        _transactions.value = Resource.loading()
        if (isNetworkAvailable()) {
            try {
                val response = api.getAllTransactions(orderId)
                withContext(Dispatchers.Main) {
                    val body = response.body()!!
                    if (response.isSuccessful) {
                        if (body.successful) _transactions.value =
                            Resource.success(body.payload)
                        else _transactions.value = Resource.error(body.message)
                    } else _transactions.value = Resource.error(response.message())
                }
            }catch (e:Exception){
                if (e is UnknownHostException) _transactions.value = Resource.networkError()
                else  _transactions.value = Resource.error(e.localizedMessage)
            }


        } else _transactions.value = Resource.networkError()
    }
}
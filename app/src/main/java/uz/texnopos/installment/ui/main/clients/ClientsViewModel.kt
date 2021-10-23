package uz.texnopos.installment.ui.main.clients

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.texnopos.installment.background.roomPersistence.BulkSms
import uz.texnopos.installment.background.roomPersistence.BulkSmsDatabase
import uz.texnopos.installment.core.Resource
import uz.texnopos.installment.core.isNetworkAvailable
import uz.texnopos.installment.data.model.Client
import uz.texnopos.installment.data.retrofit.ApiInterface
import java.net.UnknownHostException

class ClientsViewModel(private val api: ApiInterface) : ViewModel() {
    private var _clients: MutableLiveData<Resource<List<Client>>> = MutableLiveData()
    val clients get() = _clients
    fun getAllClients() {
        _clients.value = Resource.loading()
        if (isNetworkAvailable())
            viewModelScope.launch{
                load()
            }
        else _clients.value = Resource.networkError()
    }

    private suspend fun load() {
        try {
            val response = api.getAllClients()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _clients.value = Resource.success(response.body()!!.payload)
                } else {
                    _clients.value = Resource.error(response.message())
                }
            }
        } catch (e: Exception) {
            if (e is UnknownHostException)
                _clients.value = Resource.networkError()
            else _clients.value = Resource.error(e.localizedMessage)
        }

    }

    private var _all=MutableLiveData<List<BulkSms>?>()
    val allSms get() = _all
    fun all(context:Context){
        val dao=BulkSmsDatabase.getInstance(context).bulkSmsDao()
       CoroutineScope(Dispatchers.Main).launch {
            _all.value= withContext(Dispatchers.IO){ dao.all() }
       }
    }
}
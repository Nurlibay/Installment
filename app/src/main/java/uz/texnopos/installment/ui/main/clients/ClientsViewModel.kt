package uz.texnopos.installment.ui.main.clients

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.texnopos.installment.core.Resource
import uz.texnopos.installment.core.isNetworkAvailable
import uz.texnopos.installment.data.model.Client
import uz.texnopos.installment.data.retrofit.ApiInterface

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
        val response = api.getAllClients()
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                _clients.value = Resource.success(response.body()!!.payload)
            } else {
                _clients.value = Resource.error(response.message())
            }
        }
    }
}
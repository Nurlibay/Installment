package uz.texnopos.installment.ui.main.clients

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.texnopos.installment.core.Resource
import uz.texnopos.installment.core.isNetworkAvailable
import uz.texnopos.installment.data.model.SingleClient
import uz.texnopos.installment.data.retrofit.ApiInterface

class ClientsViewModel(private val api: ApiInterface) : ViewModel() {
    private var _clients: MutableLiveData<Resource<List<SingleClient>>> = MutableLiveData()
    val clients get() = _clients
    fun getAllOrders() {
        _clients.value = Resource.loading()
        if (isNetworkAvailable())
            viewModelScope.launch {
                load()
            }
        else _clients.value = Resource.networkError()
    }

    private suspend fun load() {
        val response = api.clients()
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                _clients.value = Resource.success(response.body()!!.payload)
            } else {
                _clients.value = Resource.error(response.message())
            }
        }
    }
}
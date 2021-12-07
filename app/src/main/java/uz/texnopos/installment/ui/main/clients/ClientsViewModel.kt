package uz.texnopos.installment.ui.main.clients

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.texnopos.installment.core.Constants.UNAUTHORIZED
import uz.texnopos.installment.core.Resource
import uz.texnopos.installment.core.isNetworkAvailable
import uz.texnopos.installment.data.model.Client
import uz.texnopos.installment.data.retrofit.ApiInterface
import java.net.UnknownHostException

class ClientsViewModel(private val api: ApiInterface) : ViewModel() {

    private var _clients: MutableLiveData<Resource<List<Client>>> = MutableLiveData()
    val clients get() = _clients

    fun getAllClients() = viewModelScope.launch {
        _clients.value = Resource.loading()
        if (isNetworkAvailable()) {
            try {
                val response = api.getAllClients()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val clients = response.body()!!.payload
                        _clients.value = Resource.success(clients.sortByColor())
                    } else {
                        _clients.value = Resource.error(
                            if (response.code() == 401) UNAUTHORIZED
                            else response.message()
                        )
                    }
                }
            } catch (e: Exception) {
                if (e is UnknownHostException)
                    _clients.value = Resource.networkError()
                else _clients.value = Resource.error(e.localizedMessage)
            }
        } else _clients.value = Resource.networkError()
    }
}

fun List<Client>.sortByColor(): List<Client> {
    val newClients = mutableListOf<Client>()
    this.forEach {
        if (it.color == "green") newClients.add(it)
    }
    this.forEach {
        if (it.color == "yellow") newClients.add(it)
    }
    this.forEach {
        if (it.color == "red") newClients.add(it)
    }
    return newClients.reversed()
}

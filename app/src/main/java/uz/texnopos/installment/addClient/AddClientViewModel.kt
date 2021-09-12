package uz.texnopos.installment.addClient

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response
import uz.texnopos.installment.model.ModelPhoneList
import uz.texnopos.installment.repository.Repository

class AddClientViewModel(private val repository: Repository) : ViewModel() {
    var phoneDataResponse : MutableLiveData<Response<ModelPhoneList>> = MutableLiveData()
    fun getPhoneData(){
        viewModelScope.launch {
            val response = repository.getClientData()
            phoneDataResponse.value = response
        }
    }
}
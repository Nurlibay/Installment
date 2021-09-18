package uz.texnopos.installment.ui.addClient

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response
import uz.texnopos.installment.data.model.ModelPhoneList
import uz.texnopos.installment.data.repository.Repository

class AddClientViewModel(private val repository: Repository) : ViewModel() {
    var phoneDataResponse : MutableLiveData<Response<ModelPhoneList>> = MutableLiveData()
    fun getPhoneData(){
        viewModelScope.launch {
            val response = repository.getClientData()
            phoneDataResponse.value = response
        }
    }
}
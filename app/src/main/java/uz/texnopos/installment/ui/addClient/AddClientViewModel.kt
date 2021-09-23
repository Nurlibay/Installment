package uz.texnopos.installment.ui.addClient

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response
import uz.texnopos.installment.data.model.ModelPhoneList
import uz.texnopos.installment.data.retrofit.ApiInterface
import uz.texnopos.installment.settings.Settings

class AddClientViewModel(private val api:ApiInterface, private val settings: Settings) : ViewModel() {
    var phoneDataResponse : MutableLiveData<Response<ModelPhoneList>> = MutableLiveData()
    fun getPhoneData(){
        viewModelScope.launch {

        }
    }
}
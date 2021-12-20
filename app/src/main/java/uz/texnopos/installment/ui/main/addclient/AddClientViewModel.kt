package uz.texnopos.installment.ui.main.addclient

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import uz.texnopos.installment.core.Resource
import uz.texnopos.installment.core.isNetworkAvailable
import uz.texnopos.installment.core.toMultiPart
import uz.texnopos.installment.data.model.PostClient
import uz.texnopos.installment.data.model.response.GenericResponse
import uz.texnopos.installment.data.retrofit.ApiInterface


class AddClientViewModel(private val api: ApiInterface) : ViewModel() {

    private var _register = MutableLiveData<Resource<GenericResponse<Any>>>()
    val register get() = _register

    fun register(postClient: PostClient) = viewModelScope.launch {
        _register.value = Resource.loading()
        if (isNetworkAvailable()) {
            try {
                val partMap = HashMap<String, RequestBody>()
                postClient.apply {
                    partMap["first_name"] = fullName.toRequestBody()
                    partMap["phone1"] = phone1.toRequestBody()
                    partMap["phone2"] = phone2.toRequestBody()
                    partMap["phone3"] = phone3.toRequestBody()
                    partMap["phone4"] = phone4.toRequestBody()
                    partMap["phone5"] = phone5.toRequestBody()
                    partMap["phone6"] = phone6.toRequestBody()
                    partMap["phone7"] = phone7.toRequestBody()
                    partMap["phone8"] = phone8.toRequestBody()
                    partMap["phone9"] = phone9.toRequestBody()
                    partMap["phone10"] = phone10.toRequestBody()
                }
                val file1 = postClient.passportPhoto.toMultiPart("pasport_photo")
                val file2 = postClient.letter.toMultiPart("latter")
                val response = api.clientRegister(partMap, file1, file2)
                if (response.isSuccessful) {
                    if (response.body()!!.successful) {
                        _register.value = Resource.success(response.body()!!)
                    } else _register.value = Resource.error(response.body()!!.message)
                } else {
                    _register.value = Resource.error(response.message())
                }
            } catch (e: Exception) {
                _register.value = Resource.error(e.localizedMessage)
            }
        } else _register.value = Resource.networkError()
    }
}
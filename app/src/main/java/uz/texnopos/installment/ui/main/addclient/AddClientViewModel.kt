package uz.texnopos.installment.ui.main.addclient

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import uz.texnopos.installment.core.Resource
import uz.texnopos.installment.data.model.PostClient
import uz.texnopos.installment.data.model.response.GenericResponse
import uz.texnopos.installment.data.retrofit.ApiInterface
import java.io.File

class AddClientViewModel(private val api: ApiInterface) : ViewModel() {
    private var _register = MutableLiveData<Resource<GenericResponse<Any>>>()
    val register get() = _register

    fun register(postClient: PostClient) {

        _register.value = Resource.loading()
        viewModelScope.launch {
            load(postClient)
        }
    }

    private suspend fun load(postClient: PostClient) {
        val partMap = HashMap<String, RequestBody>()
        partMap["first_name"] = postClient.first_name.toRequestBody()
        partMap["last_name"] = postClient.last_name.toRequestBody()
        partMap["middle_name"] = postClient.middle_name.toRequestBody()
        partMap["phone1"] = postClient.phone1.toRequestBody()
        partMap["phone2"] = postClient.phone2.toRequestBody()
        partMap["pasport_serial"] = postClient.pasport_serial.toRequestBody()
        partMap["pasport_number"] = postClient.pasport_number.toRequestBody()
        val file1 = postClient.pasport_photo.toMultiPart("pasport_photo")
        val file2 = postClient.latter.toMultiPart("latter")
        val response = api.clientRegister(partMap, file1, file2)
        if (response.isSuccessful) {
            if (response.body()!!.successful) {
                _register.value = Resource.success(response.body()!!)
            } else _register.value = Resource.error(response.body()!!.message)
        } else {
            _register.value = Resource.error(
                when (response.code()) {
                    401 -> "Несанкционированный"
                    else -> response.code().toString()
                }
            )
        }
    }

    private fun String.toRequestBody(): RequestBody =
        this.toRequestBody("text/plain".toMediaTypeOrNull())

    private fun File.toRequestBody(): RequestBody =
        this.asRequestBody("image/*".toMediaTypeOrNull())

    private fun File.toMultiPart(key: String) =
        MultipartBody.Part.createFormData(key, this.name, this.toRequestBody())
}
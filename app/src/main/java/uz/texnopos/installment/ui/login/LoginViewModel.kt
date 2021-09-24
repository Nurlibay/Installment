package uz.texnopos.installment.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.installment.data.Resource
import uz.texnopos.installment.data.model.PostUser
import uz.texnopos.installment.data.model.response.GenericResponse
import uz.texnopos.installment.data.retrofit.ApiInterface
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.texnopos.installment.data.model.response.UserResponse

class LoginViewModel(private val api: ApiInterface) : ViewModel() {
    private var _user = MutableLiveData<Resource<GenericResponse<UserResponse>>>()
    val user get() = _user

    fun login(user: PostUser) {
        _user.value = Resource.loading()
        viewModelScope.launch(Dispatchers.IO) {
            _login(user)
        }
    }

    private suspend fun _login(user: PostUser) {
        val response = api.login(user)
        withContext(Dispatchers.Main) {
            if(response.isSuccessful){
                if (response.body()!!.successful) {
                    _user.value = Resource.success(response.body()!!)
                }
            }
            else {
                _user.value = Resource.error(response.message())
            }
        }
    }

}
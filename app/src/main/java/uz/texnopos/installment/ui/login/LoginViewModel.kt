package uz.texnopos.installment.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.texnopos.installment.core.Resource
import uz.texnopos.installment.core.isNetworkAvailable
import uz.texnopos.installment.data.model.PostUser
import uz.texnopos.installment.data.model.response.GenericResponse
import uz.texnopos.installment.data.model.response.UserResponse
import uz.texnopos.installment.data.retrofit.ApiInterface
import uz.texnopos.installment.settings.Settings.Companion.UNAUTHORIZED

class LoginViewModel(private val api: ApiInterface) : ViewModel() {
    private var _user = MutableLiveData<Resource<GenericResponse<UserResponse>>>()
    val user get() = _user

    fun login(user: PostUser) {
        _user.value = Resource.loading()
        if (isNetworkAvailable())
            viewModelScope.launch(Dispatchers.IO) {
                load(user)
            }
        else _user.value = Resource.networkError()
    }

    private suspend fun load(user: PostUser) {
        val response = api.login(user)
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                if (response.body()!!.successful) {
                    _user.value = Resource.success(response.body()!!)
                }
            } else {
                _user.value = Resource.error(
                    when (response.code()) {
                        401 -> UNAUTHORIZED
                        else -> response.errorBody()!!.source().toString()
                    }
                )
            }
        }
    }

}
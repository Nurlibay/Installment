package uz.texnopos.installment.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.installment.core.callApi
import uz.texnopos.installment.data.Resource
import uz.texnopos.installment.data.model.LoginResponse
import uz.texnopos.installment.data.repository.Repository

class LoginViewModel(private val repo: Repository) : ViewModel() {
    private var _user = MutableLiveData<Resource<LoginResponse>>()
    val user get() = _user

    fun signWithLogin(loginResponse: LoginResponse) {
        _user.value = Resource.loading()
        callApi(repo.login(loginResponse),
            onApiSuccess = {
                _user.value= Resource.success(it!!.payload)
            },
            onApiError = {
                _user.value= Resource.error(it)
            }
        )
    }
}
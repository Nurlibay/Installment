package uz.texnopos.installment.data.repository

import retrofit2.http.GET
import uz.texnopos.installment.data.api.RestApi
import uz.texnopos.installment.data.model.LoginResponse
import uz.texnopos.installment.data.model.RequestModel

class Repository(private val api: RestApi) {
    @GET("")
    fun getClientData()=null

    fun getOrders()=api.getAllOrders()
    fun getAllClients()=api.getAllClients()
    fun login(loginResponse: LoginResponse)=api.login(loginResponse)
}
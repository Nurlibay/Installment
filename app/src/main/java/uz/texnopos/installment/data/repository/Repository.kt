package uz.texnopos.installment.data.repository

import retrofit2.http.GET
import uz.texnopos.installment.data.api.RestApi
import uz.texnopos.installment.data.model.RequestModel

class Repository(private val api: RestApi) {
    @GET("")
    fun getClientData()=null

    @GET
    fun getOrders()=api.getAllOrders()
}
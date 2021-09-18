package uz.texnopos.installment.data.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import uz.texnopos.installment.data.model.LoginResponse
import uz.texnopos.installment.data.model.ModelPhoneList
import uz.texnopos.installment.data.model.Order
import uz.texnopos.installment.data.model.RequestModel

interface RestApi {

    @GET("")
    suspend fun getPhoneData(): Response<ModelPhoneList>

    @GET("orders")
    fun getAllOrders(): Call<RequestModel<List<Order>>>

    @POST("login")
    fun login(@Body loginResponse: LoginResponse):Call<RequestModel<LoginResponse>>
}
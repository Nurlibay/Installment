package uz.texnopos.installment.data.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import uz.texnopos.installment.data.model.*

interface RestApi {

    @GET("")
    suspend fun getPhoneData(): Response<ModelPhoneList>

    @GET("orders")
    fun getAllOrders(): Call<RequestModel<List<Order>>>

    @GET("cilents")
    fun getAllClients(): Call<RequestModel<List<Client>>>

    @POST("login")
    fun login(@Body loginResponse: LoginResponse):Call<RequestModel<LoginResponse>>
}
package uz.texnopos.installment.data.retrofit

import retrofit2.Response
import retrofit2.http.*
import uz.texnopos.installment.data.model.*
import uz.texnopos.installment.data.model.response.GenericResponse
import uz.texnopos.installment.data.model.response.UserResponse

interface ApiInterface {

    @GET("")
    suspend fun getPhoneData(): Response<ModelPhoneList>

    @GET("orders")
    fun getAllOrders(): Response<RequestModel<List<Order>>>

    @GET("cilents")
    fun getAllClients(): Response<RequestModel<List<Client>>>

    @POST("api/login")
    suspend fun login(@Body user: PostUser): Response<GenericResponse<UserResponse>>
}
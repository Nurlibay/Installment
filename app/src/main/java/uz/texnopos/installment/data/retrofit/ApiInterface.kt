package uz.texnopos.installment.data.retrofit

import retrofit2.Response
import retrofit2.http.*
import uz.texnopos.installment.data.model.*
import uz.texnopos.installment.data.model.response.GenericResponse
import uz.texnopos.installment.data.model.response.UserResponse

interface ApiInterface {
    @POST("api/login")
    suspend fun login(@Body user: PostUser): Response<GenericResponse<UserResponse>>

    @POST("api/installment/pay_month")
    suspend fun payment(@Body payment: Payment):Response<GenericResponse<Any>>

    @GET("/api/cilents")
    suspend fun clients(): Response<GenericResponse<List<Client>>>

    @GET("api/order/single_client")
    suspend fun getOrders(@Query("id") clientId:Int):Response<GenericResponse<List<Order>>>

    @GET("api/transaction/get_tran")
    suspend fun getAllTransactions(@Query("id") orderId:Int):Response<GenericResponse<List<Transaction>>>
}
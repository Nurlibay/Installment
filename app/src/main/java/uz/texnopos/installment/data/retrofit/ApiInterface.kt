package uz.texnopos.installment.data.retrofit

import retrofit2.Response
import retrofit2.http.*
import uz.texnopos.installment.data.model.*
import uz.texnopos.installment.data.model.response.GenericResponse
import uz.texnopos.installment.data.model.response.Transactions
import uz.texnopos.installment.data.model.response.UserResponse

interface ApiInterface {
    @POST("api/login")
    suspend fun login(@Body user: PostUser): Response<GenericResponse<UserResponse>>

    @POST("api/installment/pay_month")
    suspend fun payment(@Body payment: Payment):Response<GenericResponse<Any>>

<<<<<<< HEAD
    @GET("/api/cilents")
    suspend fun clients(): Response<GenericResponse<List<SingleClient>>>


=======
>>>>>>> a87bbf84f3fb4778538b62896413999128992b6c
    @GET("api/orders")
    fun orders(
        @Header("Authorization") token: String,
        @Query("payload") order : Order) : Response<GenericResponse<UserResponse>>

    @GET("api/transactions")
    fun transactions(
        @Header("Authorization") token: String,
        @Query("payload") transactions : Transactions) : Response<GenericResponse<UserResponse>>
}
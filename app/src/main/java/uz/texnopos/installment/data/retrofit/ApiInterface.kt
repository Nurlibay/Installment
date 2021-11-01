package uz.texnopos.installment.data.retrofit

import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    suspend fun getAllClients(): Response<GenericResponse<List<Client>>>

    @GET("api/order/single_client")
    suspend fun getOrders(@Query("id") clientId:Int):Response<GenericResponse<List<Order>>>

    @GET("api/transaction/get_tran")
    suspend fun getAllTransactions(@Query("id") orderId:Int):Response<GenericResponse<Transactions>>

    @Multipart
    @POST("api/client/registration")
    suspend  fun clientRegister(
        @PartMap partMap: HashMap<String, RequestBody>,
        @Part filePart1: MultipartBody.Part,
        @Part filePart2: MultipartBody.Part
    ): Response<GenericResponse<Any>>
}
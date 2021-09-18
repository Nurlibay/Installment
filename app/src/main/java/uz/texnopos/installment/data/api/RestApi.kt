package uz.texnopos.installment.data.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import uz.texnopos.installment.data.model.ModelPhoneList
import uz.texnopos.installment.data.model.Order
import uz.texnopos.installment.data.model.RequestModel

interface RestApi {

    @GET("")
    suspend fun getPhoneData(): Response<ModelPhoneList>
    @Headers("token:20|q5ks4aiLGPjcheMfRPEC4AYVCv26DEmbSBF3o43r")
    @GET("orders")
    fun getAllOrders(): Call<RequestModel<List<Order>>>

}
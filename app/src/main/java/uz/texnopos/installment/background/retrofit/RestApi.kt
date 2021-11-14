package uz.texnopos.installment.background.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import uz.texnopos.installment.background.data.Phones
import uz.texnopos.installment.background.data.ReceiversModel
import uz.texnopos.installment.core.preferences.token
import uz.texnopos.installment.data.model.response.GenericResponse
import uz.texnopos.installment.di.appTimeOut
import uz.texnopos.installment.core.Constants.BASE_URL
import java.util.concurrent.TimeUnit

interface RestApi {

    @GET("api/today")
    fun getReceivers(): Call<GenericResponse<ReceiversModel>>

    @POST("api/delivered")
    fun sendDelivered(@Body phones: Phones): Call<GenericResponse<Any>>

    companion object {

        fun create(): RestApi {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val client =
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer $token")
                            .build()
                        return@addInterceptor chain.proceed(request)
                    }
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(appTimeOut, TimeUnit.SECONDS)
                    .readTimeout(appTimeOut, TimeUnit.SECONDS)
                    .writeTimeout(appTimeOut, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(RestApi::class.java)
        }
    }
}
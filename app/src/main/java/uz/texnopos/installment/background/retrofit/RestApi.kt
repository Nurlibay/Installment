package uz.texnopos.installment.background.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import uz.texnopos.installment.background.data.ReceiversModel
import uz.texnopos.installment.core.isNetworkAvailable
import uz.texnopos.installment.core.myCache
import uz.texnopos.installment.core.token
import uz.texnopos.installment.data.model.response.GenericResponse
import uz.texnopos.installment.data.retrofit.CacheInterceptor
import uz.texnopos.installment.di.baseUrl
import java.util.concurrent.TimeUnit

interface RestApi {

    @GET("api/today")
    fun getReceivers(): Call<GenericResponse<ReceiversModel>>

    companion object {

        fun create(): RestApi {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val client =
                OkHttpClient.Builder()
                    .cache(myCache)
                    .addInterceptor { chain ->
                        var request = chain.request()
                        request = if (isNetworkAvailable())
                            request.newBuilder().header("Cache-Control", "public, max-age=" + 5)
                                .build()
                        else request.newBuilder().header(
                            "Cache-Control",
                            "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                        ).build()
                        chain.proceed(request)
                    }

                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer $token")
                            .build()
                        return@addInterceptor chain.proceed(request)
                    }
                    .addNetworkInterceptor(CacheInterceptor())
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(50L, TimeUnit.SECONDS)
                    .readTimeout(50L, TimeUnit.SECONDS)
                    .writeTimeout(50L, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build()
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .baseUrl(baseUrl)
                .build()
            return retrofit.create(RestApi::class.java)
        }
    }
}
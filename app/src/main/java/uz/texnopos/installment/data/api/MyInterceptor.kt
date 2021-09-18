package uz.texnopos.installment.data.api

import okhttp3.Interceptor
import okhttp3.Response
import uz.texnopos.installment.core.getApiToken

class MyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("X-Platform", "Android")
            .addHeader("Authorization", "Bearer ${getApiToken()}")
            .build()
        return chain.proceed(request)
    }

}
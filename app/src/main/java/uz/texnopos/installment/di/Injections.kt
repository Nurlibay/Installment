package uz.texnopos.installment.di

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.texnopos.installment.core.isNetworkAvailable
import uz.texnopos.installment.core.myCache
import uz.texnopos.installment.core.token
import uz.texnopos.installment.data.retrofit.ApiInterface
import uz.texnopos.installment.data.retrofit.CacheInterceptor
import uz.texnopos.installment.settings.Constants
import uz.texnopos.installment.ui.login.LoginViewModel
import uz.texnopos.installment.ui.main.clients.ClientsViewModel
import uz.texnopos.installment.ui.main.orders.OrdersViewModel
import uz.texnopos.installment.ui.main.payment.PaymentViewModel
import uz.texnopos.installment.ui.main.transactions.TransactionsViewModel
import java.util.concurrent.TimeUnit


private const val baseUrl: String = "https://back-end.i-plan.uz/"
private const val appTimeOut = 50L

val networkModule = module {
    single {
        GsonBuilder().setLenient().create()
    }
    single {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        OkHttpClient.Builder()
            .cache(myCache)
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (isNetworkAvailable())
                    request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
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
//            .addInterceptor(ForceCacheInterceptor())
            .addInterceptor(loggingInterceptor)
            .connectTimeout(appTimeOut, TimeUnit.SECONDS)
            .readTimeout(appTimeOut, TimeUnit.SECONDS)
            .writeTimeout(appTimeOut, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }
    single {
        Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .client(get())
            .build()
    }
    single { get<Retrofit>().create(ApiInterface::class.java) }
}

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { PaymentViewModel(get()) }
    viewModel { ClientsViewModel(get()) }
    viewModel { OrdersViewModel(get()) }
    viewModel { TransactionsViewModel(get()) }
}


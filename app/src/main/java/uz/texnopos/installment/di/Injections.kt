package uz.texnopos.installment.di

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.texnopos.installment.core.token
import uz.texnopos.installment.data.retrofit.ApiInterface
import uz.texnopos.installment.settings.Settings
import uz.texnopos.installment.ui.login.LoginViewModel
import uz.texnopos.installment.ui.main.client.orders.ClientOrdersViewModel
import uz.texnopos.installment.ui.main.client.transactions.ClientTransactionsViewModel
<<<<<<< HEAD
import uz.texnopos.installment.ui.main.clients.ClientsViewModel
=======
>>>>>>> a87bbf84f3fb4778538b62896413999128992b6c
import uz.texnopos.installment.ui.main.payment.PaymentViewModel
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

val helperModule = module {
    single { Settings(androidApplication().applicationContext) }
}

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { PaymentViewModel(get()) }
<<<<<<< HEAD
    viewModel { ClientsViewModel(get()) }
=======
>>>>>>> a87bbf84f3fb4778538b62896413999128992b6c
    viewModel { ClientOrdersViewModel(get()) }
    viewModel { ClientTransactionsViewModel(get()) }
}

val adapterModule = module {

}


package uz.texnopos.installment.data.di

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.texnopos.installment.core.Constants.BASE_URL
import uz.texnopos.installment.data.api.MyInterceptor
import uz.texnopos.installment.data.api.RestApi
import uz.texnopos.installment.data.repository.Repository
import uz.texnopos.installment.ui.login.LoginViewModel
import uz.texnopos.installment.ui.orders.OrderViewModel


val viewModelModule = module {
    viewModel { OrderViewModel(get()) }
    viewModel { LoginViewModel(get()) }
}

val repositoryModule = module {
    single { Repository(get()) }
}

val retrofitModule = module {

    single { GsonBuilder().setLenient().create() }
    single {
        OkHttpClient.Builder()
            .apply { addInterceptor(MyInterceptor()) }
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }
    single { get<Retrofit>().create(RestApi::class.java) }
}

package uz.texnopos.installment.di

import org.koin.dsl.module
import uz.texnopos.installment.api.MyInterceptor

val networkModule = module {
    factory { MyInterceptor() }
}

val viewModelModule = module {

}
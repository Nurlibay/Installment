package uz.texnopos.installment.background.di

import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import uz.texnopos.installment.App
import uz.texnopos.installment.background.workers.SendBulkSmsViewModel


/**
 * Ahsen Saeed}
 * ahsansaeed067@gmail.com}
 * 10/27/19}
 */

val sendBulkSmsModule = module {

    viewModel { SendBulkSmsViewModel(androidApplication() as App, get(), get()) }
}
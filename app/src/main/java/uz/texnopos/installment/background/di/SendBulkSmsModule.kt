package uz.texnopos.installment.background.di

import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import uz.texnopos.installment.App
import uz.texnopos.installment.background.workers.SendBulkSmsViewModel


val sendBulkSmsModule = module {

    viewModel { SendBulkSmsViewModel(androidApplication() as App, get(), get()) }
}
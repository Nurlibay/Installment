package uz.texnopos.installment.background.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import uz.texnopos.installment.background.roomPersistence.BulkSmsDatabase

val roomModule = module {

    single {
        BulkSmsDatabase.getInstance(androidContext())
    }

    single {
        get<BulkSmsDatabase>().bulkSmsDao()
    }
}
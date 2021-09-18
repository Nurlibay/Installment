package uz.texnopos.installment

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import uz.texnopos.installment.core.SharedPrefUtils
import uz.texnopos.installment.data.di.apiModule
import uz.texnopos.installment.data.di.repositoryModule
import uz.texnopos.installment.data.di.retrofitModule
import uz.texnopos.installment.data.di.viewModelModule

class App : MultiDexApplication() {
    private val modules = listOf(
        repositoryModule, viewModelModule, retrofitModule, apiModule
    )

    override fun onCreate() {
        super.onCreate()
        appInstance = this
        startKoin {
            androidLogger()
            androidContext(this@App)
            androidFileProperties()
            koin.loadModules(modules)
        }

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {
        private lateinit var appInstance: App
        var sharedPrefUtils: SharedPrefUtils? = null

        fun getAppInstance(): App {
            return appInstance
        }
    }
}
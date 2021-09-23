package uz.texnopos.installment

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import uz.texnopos.installment.di.adapterModule
import uz.texnopos.installment.di.helperModule
import uz.texnopos.installment.di.networkModule
import uz.texnopos.installment.di.viewModelModule

class App : MultiDexApplication() {
    private val modules = listOf(
        networkModule, viewModelModule, helperModule, adapterModule
    )

    override fun onCreate() {
        super.onCreate()
        appInstance=this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val modules = listOf(helperModule, viewModelModule, adapterModule, networkModule)
        startKoin { // use AndroidLogger as Koin Logger - default Level.INFO
            androidLogger()

            // use the Android context given there
            androidContext(this@App)

            // load properties from assets/koin.properties file
            androidFileProperties()

            // module list
            koin.loadModules(modules)
        }

    }
    companion object{
        private lateinit var appInstance:App
        fun getAppInstance(): App {
            return appInstance
        }
    }
}
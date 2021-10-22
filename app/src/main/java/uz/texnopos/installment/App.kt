package uz.texnopos.installment

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import uz.texnopos.installment.background.Utils
import uz.texnopos.installment.background.Utils.minute
import uz.texnopos.installment.background.di.sendBulkSmsModule
import uz.texnopos.installment.core.isSignedIn
import uz.texnopos.installment.core.preferences.SharedPrefUtils
import uz.texnopos.installment.di.networkModule
import uz.texnopos.installment.di.viewModelModule

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        appInstance=this

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val modules = listOf(viewModelModule, networkModule, sendBulkSmsModule)
        startKoin { // use AndroidLogger as Koin Logger - default Level.INFO
            androidLogger()

            // use the Android context given there
            androidContext(this@App)

            // load properties from assets/koin.properties file
            androidFileProperties()

            // module list
            koin.loadModules(modules)
        }
        if (isSignedIn()) Utils.setAlarm(this, System.currentTimeMillis() + 1 * minute)
    }

    companion object {
        const val APPLICATION_CHANNEL = "spartons.com.prosmssenderapp.send_sms_notification"
        private const val APPLICATION_NOTIFICATION = "Send Sms Notification"

        private lateinit var appInstance: App
        var sharedPrefUtils: SharedPrefUtils? = null
        fun getAppInstance(): App {
            return appInstance
        }
    }
}
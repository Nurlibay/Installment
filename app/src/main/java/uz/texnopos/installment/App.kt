package uz.texnopos.installment

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import uz.texnopos.installment.background.Utils
import uz.texnopos.installment.core.preferences.SharedPrefUtils
import uz.texnopos.installment.di.networkModule
import uz.texnopos.installment.di.viewModelModule
import uz.texnopos.installment.core.Constants.MINUTE

class App : MultiDexApplication() {
    private lateinit var analytics: FirebaseAnalytics
    override fun onCreate() {
        super.onCreate()
        appInstance=this
        analytics= FirebaseAnalytics.getInstance(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val modules = listOf(viewModelModule, networkModule)
        startKoin { // use AndroidLogger as Koin Logger - default Level.INFO
            androidLogger()

            // use the Android context given there
            androidContext(this@App)

            // load properties from assets/koin.properties file
            androidFileProperties()

            // module list
            koin.loadModules(modules)
        }
        Utils.setAlarm(this, System.currentTimeMillis() + 5 * MINUTE)
    }

    companion object {
        private lateinit var appInstance: App
        var sharedPrefUtils: SharedPrefUtils? = null
        fun getAppInstance(): App {
            return appInstance
        }
    }
}
package uz.texnopos.installment.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import uz.texnopos.installment.R
import uz.texnopos.installment.core.AppBaseActivity
import uz.texnopos.installment.databinding.ActivityMainBinding

class MainActivity : AppBaseActivity() {

    private lateinit var analytics: FirebaseAnalytics
    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            navHostFragment = supportFragmentManager.findFragmentById(R.id.fcvMain) as NavHostFragment
            navController = navHostFragment.findNavController()
        }
        setContentView(binding.root)
        openPowerSettings(this)
    }

    private fun openPowerSettings(context: Context) {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        if (powerManager.isIgnoringBatteryOptimizations(context.packageName)) {
            val intent = Intent()
            intent.action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
            context.startActivity(intent)
        }
    }
}
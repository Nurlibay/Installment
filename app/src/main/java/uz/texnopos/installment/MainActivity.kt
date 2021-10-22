package uz.texnopos.installment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import uz.texnopos.installment.background.util.Constants.ASK_SMS_PERMISSION_REQUEST_CODE
import uz.texnopos.installment.background.util.askPermission
import uz.texnopos.installment.core.AppBaseActivity
import uz.texnopos.installment.databinding.ActivityMainBinding

class MainActivity : AppBaseActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }



}
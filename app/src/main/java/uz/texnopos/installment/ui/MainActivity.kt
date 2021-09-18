package uz.texnopos.installment.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import uz.texnopos.installment.R
import uz.texnopos.installment.base.AppBaseActivity
import uz.texnopos.installment.databinding.ActivityMainBinding

class MainActivity : AppBaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}
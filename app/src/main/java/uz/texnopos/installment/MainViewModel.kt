package uz.texnopos.installment

import androidx.lifecycle.ViewModel
import uz.texnopos.installment.data.retrofit.ApiInterface
import uz.texnopos.installment.settings.Settings

class MainViewModel(private val settings: Settings, private val api: ApiInterface): ViewModel(){

}
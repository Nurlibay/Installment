package uz.texnopos.installment.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import spartons.com.prosmssenderapp.helper.SharedPreferenceHelper
import timber.log.Timber
import uz.texnopos.installment.App.Companion.getAppInstance
import uz.texnopos.installment.background.retrofit.RestApi
import uz.texnopos.installment.background.roomPersistence.BulkSmsDatabase
import uz.texnopos.installment.background.workers.SendBulkSmsViewModel
import uz.texnopos.installment.core.callApi
import uz.texnopos.installment.settings.Constants.mySharedPreferences

class BootCompleteReceiver : BroadcastReceiver() {
    private val sharedPreference =
        getAppInstance().getSharedPreferences(mySharedPreferences, Context.MODE_PRIVATE)
    private val dao = BulkSmsDatabase.getInstance(getAppInstance()).bulkSmsDao()
    private val sharedPreferenceHelper = SharedPreferenceHelper(sharedPreference)
    val viewModel = SendBulkSmsViewModel(getAppInstance(), sharedPreferenceHelper, dao)
    override fun onReceive(context: Context?, intent: Intent?) {

        val apiInterface = RestApi.create().getReceivers()
        callApi(apiInterface,
            onApiSuccess = {
                if (it != null) {
                    if (it.payload.clients.isNotEmpty()) {
                        val smsText = it.payload.sms
                        val clients = it.payload.clients
                        val phoneNumbers = arrayListOf<String>()
                        for (client in clients){
                            phoneNumbers.add(client.phone1)
                            phoneNumbers.add(client.phone2)
                        }
                        viewModel.sendBulkSms(phoneNumbers, smsText, clients)
                    }
                }
            },
            onApiError = {
                Timber.d("onReceive: error: $it ")
            })

        Timber.d("onReceive: start1")
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            Timber.d("onReceive: start2")

//            Utils.setAlarm(context, timeInMilli)
        }
    }

}


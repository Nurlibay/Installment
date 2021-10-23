package uz.texnopos.installment.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import timber.log.Timber
import uz.texnopos.installment.App.Companion.getAppInstance
import uz.texnopos.installment.background.data.Client
import uz.texnopos.installment.background.retrofit.RestApi
import uz.texnopos.installment.background.roomPersistence.BulkSmsDatabase
import uz.texnopos.installment.background.workers.SendBulkSmsViewModel
import uz.texnopos.installment.core.callApi
import uz.texnopos.installment.settings.Constants.TAG

class BootCompleteReceiver : BroadcastReceiver() {
    private val dao = BulkSmsDatabase.getInstance(getAppInstance()).bulkSmsDao()
    val viewModel = SendBulkSmsViewModel(getAppInstance(), dao)
    override fun onReceive(context: Context?, intent: Intent?) {

        val apiInterface = RestApi.create().getReceivers()
        callApi(apiInterface,
            onApiSuccess = {
                if (it != null) {
                    if (it.payload.clients.isNotEmpty()) {
                        val smsText = it.payload.sms
                        val clients = it.payload.clients as ArrayList<Client>
                        viewModel.sendBulkSms(clients, smsText)
                    }
                }
            },
            onApiError = {
                Timber.d("onReceive: error: $it ")
            })

        Timber.d("onReceive: start1")
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            Timber.d("onReceive: start2")
        }
    }

}


package uz.texnopos.installment.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber
import uz.texnopos.installment.App
import uz.texnopos.installment.background.data.Client
import uz.texnopos.installment.background.retrofit.RestApi
import uz.texnopos.installment.background.roomPersistence.BulkSmsDatabase
import uz.texnopos.installment.background.workers.SendBulkSmsViewModel
import uz.texnopos.installment.core.callApi

class TimeChangedReceiver : BroadcastReceiver() {
    private val dao = BulkSmsDatabase.getInstance(App.getAppInstance()).bulkSmsDao()
    val viewModel = SendBulkSmsViewModel(App.getAppInstance(), dao)
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
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            Timber.d("onReceive: start2")
        }
    }
}
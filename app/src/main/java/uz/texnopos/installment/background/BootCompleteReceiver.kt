package uz.texnopos.installment.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber
import uz.texnopos.installment.App.Companion.getAppInstance
import uz.texnopos.installment.background.data.Client
import uz.texnopos.installment.background.retrofit.RestApi
import uz.texnopos.installment.background.roomPersistence.BulkSmsDatabase
import uz.texnopos.installment.background.workers.SendBulkSmsViewModel
import uz.texnopos.installment.core.callApi
import uz.texnopos.installment.core.isNetworkAvailable

class BootCompleteReceiver : BroadcastReceiver() {
    private val dao = BulkSmsDatabase.getInstance(getAppInstance()).bulkSmsDao()
    val viewModel = SendBulkSmsViewModel(getAppInstance(), dao)
    override fun onReceive(context: Context?, intent: Intent?) {
        val api = RestApi.create()
        if (isNetworkAvailable())
            try {
                callApi(api.getReceivers(),
                    onApiSuccess = {
                        if (it != null) {
                            val data = it.payload
                            if (it.successful) {
                                val smsText = data.sms
                                if (data.clients.isNotEmpty()) {
                                    val clients = data.clients.filter { c ->
                                        c.phone1.length == 13 ||
                                                c.phone2.length == 9
                                    } as ArrayList<Client>
                                    viewModel.sendBulkSms(clients, smsText)
                                }
                            }
                        }
                    })
            } catch (e: Exception) {

            }

        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            Timber.d("onReceive: start2")
        }
    }

}


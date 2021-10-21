package uz.texnopos.installment.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import okhttp3.Callback
import retrofit2.Call
import retrofit2.Response
import uz.texnopos.installment.background.retrofit.RestApi
import uz.texnopos.installment.core.callApi
import uz.texnopos.installment.data.model.Client
import uz.texnopos.installment.data.model.Payment
import uz.texnopos.installment.data.model.response.GenericResponse

class BootCompleteReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val apiInterface = RestApi.create().payment(Payment(135,1000.0))
        callApi(apiInterface,
        onApiSuccess = {
            Log.d("tekseriw", "onReceive: paid} ")
        },
        onApiError = {
            Log.d("tekseriw", "onReceive: error: $it ")
        })

        Log.d("tekseriw", "onReceive: start1")
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            Log.d("tekseriw", "onReceive: start2")

//            Utils.setAlarm(context, timeInMilli)
        }
    }

}


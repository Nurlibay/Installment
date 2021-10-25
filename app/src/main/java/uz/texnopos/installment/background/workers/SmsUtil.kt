package uz.texnopos.installment.background.workers

import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.telephony.SmsManager
import uz.texnopos.installment.background.Utils.flags
import uz.texnopos.installment.core.toast
import java.util.*

class SMSUtils : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == SENT_SMS_ACTION_NAME) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    context.toast("Sms send")
                    isSent = true
                }
                SmsManager.RESULT_ERROR_GENERIC_FAILURE -> context.toast("Sms not send")
                SmsManager.RESULT_ERROR_NO_SERVICE -> context.toast("Sms not send no service")
                SmsManager.RESULT_ERROR_NULL_PDU -> context.toast("Sms not send")
                SmsManager.RESULT_ERROR_RADIO_OFF -> context.toast("Sms not send no radio)")
            }
        } else if (intent.action == DELIVERED_SMS_ACTION_NAME) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    context.toast("Sms receive")
                    isSent = true
                }
                Activity.RESULT_CANCELED -> context.toast("Sms not receive")
            }
        }
    }

    companion object {
        const val SENT_SMS_ACTION_NAME = "SMS_SENT"
        const val DELIVERED_SMS_ACTION_NAME = "SMS_DELIVERED"

        var isSent = false

        private fun canSendSMS(context: Context): Boolean {
            return context.packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)
        }

        fun sendSMS(context: Context, phoneNumber: String?, message: String?): Boolean {
            if (!canSendSMS(context)) {
                context.toast("Cannot send sms)")
                return false
            }
            val sentPI = PendingIntent.getBroadcast(context, 0, Intent(SENT_SMS_ACTION_NAME), flags)
            val deliveredPI = PendingIntent.getBroadcast(context, 0, Intent(
                DELIVERED_SMS_ACTION_NAME), flags)
            val smsUtils = SMSUtils()
            //register for sending and delivery
            context.registerReceiver(smsUtils, IntentFilter(SENT_SMS_ACTION_NAME))
            context.registerReceiver(smsUtils, IntentFilter(DELIVERED_SMS_ACTION_NAME))
            val sms = SmsManager.getDefault()
            val parts = sms.divideMessage(message)
            val sendList = ArrayList<PendingIntent>()
            sendList.add(sentPI)
            val deliverList = ArrayList<PendingIntent>()
            deliverList.add(deliveredPI)
            sms.sendMultipartTextMessage(phoneNumber, null, parts, sendList, deliverList)

            //we unsubscribed in 10 seconds
            Handler(Looper.getMainLooper()).postDelayed({ context.unregisterReceiver(smsUtils) },
                10000)
            return isSent
        }
    }
}
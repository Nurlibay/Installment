package uz.texnopos.installment.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.Activity

class MessageSentListener : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        val resultCode = this.resultCode
        val successfullySent = resultCode == Activity.RESULT_OK
        //SMSForwarderApp.getAppContext().unregisterReceiver(this)
    }
}
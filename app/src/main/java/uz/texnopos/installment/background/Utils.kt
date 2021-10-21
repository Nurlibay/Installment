package uz.texnopos.installment.background

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

object Utils {
    val minute=60*1000L
    fun setAlarm(context: Context, timeOfAlarm: Long) {

        // Intent to start the Broadcast Receiver
        val broadcastIntent = Intent(context, BootCompleteReceiver::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val pIntent = PendingIntent.getBroadcast(
                context,
                0,
                broadcastIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            // Setting up AlarmManager
            val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            if (System.currentTimeMillis() < timeOfAlarm) {
                alarmMgr.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    timeOfAlarm,
                    5 * minute,
                    pIntent
                )
            }
        }

    }
}
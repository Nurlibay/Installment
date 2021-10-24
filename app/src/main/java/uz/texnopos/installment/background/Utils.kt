package uz.texnopos.installment.background

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import uz.texnopos.installment.settings.Constants.MINUTE

object Utils {
    const val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    fun setAlarm(context: Context, timeOfAlarm: Long) {

        val broadcastIntent = Intent(context, BootCompleteReceiver::class.java)
        val pIntent = PendingIntent.getBroadcast(context, 0, broadcastIntent, flags)

        // Setting up AlarmManager
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (System.currentTimeMillis() < timeOfAlarm) {
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                timeOfAlarm,
                5 * MINUTE,
                pIntent
            )
        }
    }
}
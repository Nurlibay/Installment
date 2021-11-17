package uz.texnopos.installment.firebase.cloudMessaging

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import uz.texnopos.installment.R

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private var broadcaster: LocalBroadcastManager? = null
    private val processLater = false

    override fun onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this)
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fcm_token", token).apply()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d(TAG, "From: ${remoteMessage.from}")


        if (/* Check if data needs to be processed by long running job */ processLater) {
            //scheduleJob()
            Log.d(TAG, "executing schedule job")
        } else {
            // Handle message within 10 seconds
            handleNow(remoteMessage)
        }
    }

    private fun handleNow(remoteMessage: RemoteMessage) {
        val handler = Handler(Looper.getMainLooper())

        handler.post {
            Toast.makeText(baseContext, getString(R.string.handle_notification_now), Toast.LENGTH_LONG).show()

            remoteMessage.notification?.let {
                val intent = Intent("MyData")
                intent.putExtra("message", remoteMessage.data["text"])
                broadcaster?.sendBroadcast(intent)
            }
        }
    }

    companion object {
        private const val TAG = "MyFirebaseMessagingS"
    }
}
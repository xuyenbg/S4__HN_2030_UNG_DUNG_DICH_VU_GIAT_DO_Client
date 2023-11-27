package datn.fpoly.myapplication.utils

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.ui.splashScreen.SplashsActivity


class ServiceMessageFirebase : FirebaseMessagingService() {

    companion object {
        val CHANNEL_ID ="datn.fpoly.myapplication"
        val idNotify =999
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        notifycationCustom(message,this)

    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    private fun notifycationCustom(message: RemoteMessage, context: Context) {
        try {
            val intent = Intent(this, SplashsActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            builder.setSmallIcon(R.mipmap.ic_launcher)
            builder.setContentTitle(message.notification?.title)
            builder.setContentText(message.notification?.body)
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT)
            builder.setContentIntent(pendingIntent)
            builder.setAutoCancel(true)

            val notificationManager = NotificationManagerCompat.from(this)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }
            notificationManager.notify(idNotify, builder.build())
        }catch (e: Exception){}


    }
}
package datn.fpoly.myapplication.utils

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import datn.fpoly.myapplication.ui.splashScreen.SplashsActivity


class ServiceMessageFirebase : FirebaseMessagingService() {

    companion object {
        val CHANNEL_ID ="datn.fpoly.myapplication"
        val idNotify =999
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.e("AAAAAAAAAAAA", "onMessageReceived: "+message.notification?.title )
        Log.e("AAAAAAAAAAAA", "onMessageReceived: "+message.notification?.body )
        message.notification?.title?.let { message.notification?.body?.let { it1 ->
            showNotification(it,
                it1
            )
        } }

    }

    private fun showNotification(title: String, body: String) {
        // Tạo Intent để mở Activity khi người dùng nhấn vào thông báo
//        val intent = Intent(this, SplashsActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        // Tạo đối tượng Notification
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSmallIcon(datn.fpoly.myapplication.R.mipmap.ic_launcher)
//                .setContentIntent(pendingIntent)

        // Hiển thị thông báo
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }


}
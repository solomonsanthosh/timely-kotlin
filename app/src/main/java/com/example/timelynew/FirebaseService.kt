package com.example.timelynew

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.timelynew.dataClass.Activity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseService:FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)


        Log.i("NOTIFsssssssssssssssssssssssssssssssssssssssssssssssssssss",message.notification!!.title.toString())
//        if (message.getNotification() != null) {
//
//            var title : String = message.notification!!.title!!
//            var message : String = message.notification!!.body!!
//
//
//
//
//            var builder: NotificationCompat.Builder;
//
//            val notificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            //val notificationManager = NotificationManagerCompat.from(applicationContext)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                val channelID = "1"
//                val channelName = "notify"
//                val notificationChannel =
//                    NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
//                notificationManager.createNotificationChannel(notificationChannel)
//                builder = NotificationCompat.Builder(applicationContext, notificationChannel.id)
//            } else {
//                builder = NotificationCompat.Builder(applicationContext)
//            }
//
//            builder = builder.setSmallIcon(R.drawable.baseline_notifications_active_24)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
//                .setOnlyAlertOnce(true)
//
//
//            notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
//        }
    }
    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.i("Tokenssssssssss", s.toString())
        getSharedPreferences("token", MODE_PRIVATE).edit().putString("fb", s).apply()

    }



}
package com.example.timelynew.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.timelynew.R


class NotificationReceiver:BroadcastReceiver() {
    val CHANNEL_ID = "1"
    override fun onReceive(context: Context?, intent: Intent?) {
        val text = intent?.getStringExtra("text")
       var builder = NotificationCompat.Builder(context!!,CHANNEL_ID)
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID,"1",NotificationManager.IMPORTANCE_DEFAULT)
            val manager : NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)

            builder.setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setContentTitle("Remainder")
                .setContentText(text)

        } else {
            builder.setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setContentTitle("Remainder")
                .setContentText(text)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
        }
        val notificationManagerCompat:NotificationManagerCompat = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManagerCompat.notify(1,builder.build())
    }
}
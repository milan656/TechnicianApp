package com.walkins.technician.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.technician.common.PrefManager
import com.walkins.technician.R
import java.util.*

class HomeSubDealerActivity : AppCompatActivity() {

    var title: String? = "Notification Title"
    var content = "Content"
    var type: String? = ""
    var action: String? = null
    var imageURL: String? = null
    var body: String? = null
    var notification_data: String? = null
    var technician_channel: String? = "technician_channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_sub_dealer)

        sendNotification(title, body, action, notification_data)
    }

    private fun sendNotification(
        title: String?,
        messageBody: String?,
        type: String?,
        notification_data: String?
    ) {
        var type = type
        if (type == null) {
            type = ""
        }
        val m = (Date().time / 1000L % Int.MAX_VALUE).toInt()
        var intent: Intent? = null
        intent = Intent(this, MainActivity::class.java)
        intent.putExtra("isFromNotification", "notification")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, m, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val defaultSoundUri =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val color = resources.getColor(R.color.yellow)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val options = BitmapFactory.Options()
        options.inScaled = false
        val source: Bitmap? = null

        val notificationBuilder =
            NotificationCompat.Builder(this, technician_channel!!)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.mipmap.ic_walkins_logo)
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_walkins_logo)
        }

        Log.e("noti", "" + body)
        if (imageURL == null || imageURL == "") {
            val bigText =
                NotificationCompat.BigTextStyle()
            bigText.bigText(body)

            notificationBuilder.setColor(color)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setChannelId(technician_channel!!)
                .setContentIntent(pendingIntent)
                .setStyle(bigText)
        } else {
            Log.e("noti", ":::;" + body)
            val bigText =
                NotificationCompat.BigTextStyle()
            bigText.bigText(body)

            notificationBuilder.setColor(color)
                .setContentTitle(title)
                .setContentText(body)
                .setLargeIcon(source)
                .setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(source).bigLargeIcon(null).setSummaryText(messageBody)
                ).setStyle(bigText)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setChannelId(technician_channel!!)
                .setContentIntent(pendingIntent)
        }
        var mChannel: NotificationChannel?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = NotificationChannel(
                technician_channel!!,
                "technician",
                NotificationManager.IMPORTANCE_HIGH
            )
            // Configure the notification channel.
            mChannel.enableLights(true)
            mChannel.lightColor = Color.RED
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager.createNotificationChannel(mChannel)
            notificationManager.notify(m, notificationBuilder.build())
        } else {
            notificationManager.notify(m, notificationBuilder.build())
        }
    }
}
package com.jkadvantage.fcm

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
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.technician.common.Common
import com.example.technician.common.PrefManager
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.activity.HomeEmployeeActivity
import com.walkins.aapkedoorstep.activity.HomeSubDealerActivity
import com.walkins.aapkedoorstep.activity.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by rtt on 19-Jan-17.
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {
    var title: String? = ""
    var content = ""
    var type: String? = ""
    var action: String? = null
    var imageURL: String? = null
    var body: String? = null
    var notification_data: String? = null
    var prefManager: PrefManager? = null
    var technician_channel: String? = "fcm_default_channel"

    override fun onNewToken(refreshedToken: String) {
        super.onNewToken(refreshedToken)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        try {
            val data = remoteMessage.data

            Log.e("data", "+++++++++" + data)

            if (data.containsKey("type")) {
                title = data["title"]
                body = data["body"]
                action = data["action"]
                imageURL = data["imageURL"]
                type = data["type"]
                notification_data = data["notification_data"]
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        /* title = "Notification Title"
         body =
             "Notification body Description text message.Notification body Description text Notification body Description text message.Notification body Description text message.Notification body Description text message.Notification body Description text message.Notification body Description text message.Notification body Description text message.Notification body Description text message.Notification body Description text message.Notification body Description text message.Notification body Description text message."
         notification_data = "data"
         action = "action"*/
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
        prefManager = PrefManager(this)
        val m = (Date().time / 1000L % Int.MAX_VALUE).toInt()
        var intent: Intent? = null
        intent = Intent(this, MainActivity::class.java)
//        intent.putExtra("isFromNotification", "profile")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, m, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val defaultSoundUri =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val options = BitmapFactory.Options()
        options.inScaled = false
        val source: Bitmap? = null

        val notificationBuilder =
            NotificationCompat.Builder(this, technician_channel!!)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.ic_app_icon_transparent)
        } else {
            notificationBuilder.setSmallIcon(R.drawable.ic_app_icon_transparent)
        }

        Log.e("noti", "" + body)
        if (imageURL == null || imageURL == "") {
            val bigText =
                NotificationCompat.BigTextStyle()
            bigText.bigText(body)

            notificationBuilder
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

            notificationBuilder
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
                technician_channel,
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

    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection =
                url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}
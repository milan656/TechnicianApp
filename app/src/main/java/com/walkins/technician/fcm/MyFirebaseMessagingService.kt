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
import com.walkins.technician.R
import com.walkins.technician.activity.HomeEmployeeActivity
import com.walkins.technician.activity.HomeSubDealerActivity
import com.walkins.technician.activity.MainActivity
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
        intent =
            if (prefManager?.getValue("customerClass") != null && prefManager?.getValue("customerClass")
                    .equals("na", ignoreCase = true)
            ) {
                Intent(this, HomeSubDealerActivity::class.java)
            }else if (prefManager?.getValue("customerClass") != null && prefManager?.getValue("customerClass")
                    .equals("hy", ignoreCase = true)
            ) {
                Intent(this, HomeSubDealerActivity::class.java)
            } else {
                if (prefManager!!.getType() != null) {
                    when (prefManager!!.getType()) {
                        "area_manager" -> Intent(this, HomeEmployeeActivity::class.java)
                        "head_office" -> Intent(this, HomeEmployeeActivity::class.java)
                        "zone_manager", "ztm" -> Intent(this, HomeEmployeeActivity::class.java)
                        "region_manager" -> Intent(this, HomeEmployeeActivity::class.java)
                        "sub_dealer" -> Intent(this, HomeSubDealerActivity::class.java)
                        "branch" -> Intent(this, HomeSubDealerActivity::class.java)
                        else -> Intent(this, MainActivity::class.java)
                    }
                } else {
                    Intent(this, MainActivity::class.java)
                }
            }

//        type = "yearly_offtake"

        if (type?.equals("dashboard")) {
            intent.putExtra("isFromNotification", "dashboard")
        } else if (type?.equals("trade-scheme")) {
            intent.putExtra("isPcr", true)
        } else if (type?.equals("gift-catalogue")) {
            intent.putExtra("isFromNotification", "gift-catalogue")
        } else if (type?.equals("merchandise-catalogue")) {
            intent.putExtra("isFromNotification", "merchandise-catalogue")
        } else if (type?.equals("dashboard_ytd")) {
            intent.putExtra("isFromNotification", "dashboard_ytd")
        } else if (type?.equals("dashboard_mtd")) {
            intent.putExtra("isFromNotification", "dashboard_mtd")
        } else if (type?.equals("dashboard_qtd")) {
            intent.putExtra("isFromNotification", "dashboard_qtd")
        } else if (type?.equals("yearly_offtake")) {
            intent.putExtra("isFromNotification", "yearly_offtake")
        } else if (type?.equals("report")) {
            intent.putExtra("isFromNotification", "report")
        } else if (type?.equals("service_preference")) {
            intent.putExtra("isFromNotification", "service_preference")
        } else if (type?.equals("profile")) {
            intent.putExtra("isFromNotification", "profile")
        } else if (type?.equals("profile_request_to_edit")) {
            intent.putExtra("isFromNotification", "profile_request_to_edit")
        } else if (type?.equals("add_warranty")) {
            intent.putExtra("isFromNotification", "add_warranty")
        } else if (type?.equals("about_programme")) {
            intent.putExtra("isFromNotification", "about_programme")
        } else if (type?.equals("warranty_scorecard")) {
            intent.putExtra("isFromNotification", "warranty_scorecard")
        } else if (action.equals("warranty_report")) {
            intent.putExtra("isFromNotification", "warranty_scorecard")
        } else if (type?.equals("merchandise")) {
            intent.putExtra("isFromNotification", "merchandise")
        } else if (type?.equals("check_warranty_eligibility_warranty")) {
            intent.putExtra("isFromNotification", "check_warranty_eligibility_warranty")
        } else if (type?.equals("update_app")) {
            intent.putExtra("isFromNotification", "update_app")
        } else if (type?.equals("contact_us")) {
            intent.putExtra("isFromNotification", "contact_us")
        } else if (type?.equals("logout")) {
            intent.putExtra("isFromNotification", "logout")
        } else if (type.equals("invite_branch_sub_dealer")) {
            intent.putExtra("isFromNotification", "invite_branch_sub_dealer")
        } else if (type.equals("add_customer")) {
            intent.putExtra("isFromNotification", "add_customer")
        } else if (type.equals("customer_list")) {
            intent.putExtra("isFromNotification", "customer_list")
        } else if (type.equals("manage_group")) {
            intent.putExtra("isFromNotification", "manage_group")
        } else if (type.equals("create_campaign")) {
            intent.putExtra("iscampaign", "iscampaign")
            intent.putExtra("isCreated", true)
        } else if (type.equals("buy_sms")) {
            intent.putExtra("isFromNotification", "buy_sms")
        } else if (type.equals("order_history")) {
            intent.putExtra("isFromNotification", "order_history")
        } else if (type.equals("LOW_BALANCE")) {
            intent.putExtra("isFromNotification", "LOW_BALANCE")
        } else if (type?.equals("employee_dashboard")) {
            intent.putExtra("isFromNotification", "dashboard")
        } else if (type?.equals("employee_dashboard_ytd")) {
            intent.putExtra("isFromNotification", "ytd")
        } else if (type?.equals("employee_dashboard_mtd")) {
            intent.putExtra("isFromNotification", "mtd")
        } else if (type?.equals("employee_dashboard_qtd")) {
            intent.putExtra("isFromNotification", "qtd")
        } else if (type?.equals("employee_yearly_offtake")) {
            intent.putExtra("isFromNotification", "yearly_offtake")
        } else if (type?.equals("employee_warranty_scorecard")) {
            intent.putExtra("isFromNotification", "employee_warranty_scorecard")
        } else if (type?.equals("employee_logout")) {
            intent.putExtra("isFromNotification", "employee_logout")
        } else if (type?.equals("employee_update_app")) {
            intent.putExtra("isFromNotification", "employee_update_app")
        } else if (type?.equals("employee_offtake_scorecard")) {
            intent.putExtra("isFromNotification", "employee_offtake_scorecard")
        } else if (type?.equals("poster_download")) {
            intent.putExtra("isFromNotification", "poster_download")
        }else if (type?.equals("redeem_coupon_report")) {
            intent.putExtra("isFromNotification", "redeem_coupon_report")
        } else if (type?.equals("campaign")) {
            intent.putExtra("iscampaign", true)
            intent.putExtra("filterTypeName", "")

            if (notification_data != null && !notification_data?.equals("")) {
                val formatter = SimpleDateFormat("dd-MM-yyyy")
                val gson = Gson()
                try {
                    val jsonObject = JSONObject(notification_data)
                    if (jsonObject.has("filter_type")) {

                        val filter_type = jsonObject.getString("filter_type")
                        when (filter_type) {
                            "occasion" -> {
                                var occasion: String? = null

                                var custom_filter: String? = null
                                if (jsonObject?.has("custom_filter")!!) {
                                    custom_filter = jsonObject?.getString("custom_filter")
                                }

                                if (custom_filter != null && !custom_filter?.equals("")!!) {
                                    if (custom_filter?.equals("birthday_wish")!!) {
                                        occasion = "birthday"
                                    } else {
                                        occasion = "anniversary"

                                    }
                                }
                                val date = Date()
                                val dateObj: String = formatter.format(date)
                                var occasion_date_from = Common?.datefrom(dateObj)
                                var occasion_date_to = Common?.dateTo(dateObj)
                                var purpose_id: String? = null
                                if (jsonObject?.has("purpose_id")!!) {
                                    purpose_id = jsonObject?.getString("purpose_id")
                                }


                                /*val uploadCustomerModel = UploadCampaignModel(
                                    false,
                                    true,
                                    filter_type,
                                    null,
                                    null,
                                    occasion,
                                    occasion_date_from,
                                    occasion_date_to,
                                    null,
                                    null,
                                    null,
                                    null,
                                    ArrayList<String>(),
                                    null,
                                    ArrayList<String>(),
                                    ArrayList<String>(),
                                    null,
                                    null,
                                    null, null, null, null, null, null
                                )

                                intent.putExtra(
                                    "customFilterModel",
                                    gson.toJson(uploadCustomerModel)
                                )*/
                                intent.putExtra("purpose_id", purpose_id)

                            }


                            "festival" -> {

                                var purpose_id: String? = null
                                if (jsonObject?.has("purpose_id")!!) {
                                    purpose_id = jsonObject?.getString("purpose_id")
                                }

                                var festival_id: String? = null
                                if (jsonObject?.has("festival_id")!!) {
                                    festival_id = jsonObject?.getString("festival_id")
                                }
                                intent.putExtra("isCreated", true)
                                intent.putExtra("purpose_id", purpose_id)
                                intent.putExtra("festival_id", festival_id)


                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                intent.putExtra("iscampaign", "iscampaign")
                intent.putExtra("isCreated", false)

            }

        } else if (type?.equals("customer_management")) {
            intent.putExtra("isCustomer", true)

            if (notification_data != null && !notification_data?.equals("")) {
                val formatter = SimpleDateFormat("dd-MM-yyyy")
                val gson = Gson()
                try {
                    val jsonObject = JSONObject(notification_data)
                    if (jsonObject.has("filter_type")) {

                        val filter_type = jsonObject.getString("filter_type")
                        when (filter_type) {
                            "due" -> {

                                var custom_filter: String? = null
                                if (jsonObject?.has("custom_filter")!!) {
                                    custom_filter = jsonObject?.getString("custom_filter")
                                }
                                var due_date_from: String? = null
                                if (jsonObject?.has("due_date_from")!!) {
                                    due_date_from = jsonObject?.getString("due_date_from")
                                }
                                var due_date_to: String? = null
                                if (jsonObject?.has("due_date_to")!!) {
                                    due_date_to = jsonObject?.getString("due_date_to")
                                }
                                var purpose_id: String? = null
                                if (jsonObject?.has("purpose_id")!!) {
                                    purpose_id = jsonObject?.getString("purpose_id")
                                }
                                var custom_filter_purpose_id: String? = null
                                if (jsonObject?.has("custom_filter_purpose_id")!!) {
                                    custom_filter_purpose_id =
                                        jsonObject?.getString("custom_filter_purpose_id")
                                }
                                var due_type: String? = null
                                if (jsonObject?.has("due_type")!!) {
                                    due_type =
                                        jsonObject?.getString("due_type")
                                }
                                var serviceList = ArrayList<String>()

                                if (jsonObject?.has("custom_filter_service_id")!!) {
                                    val jsonArray =
                                        jsonObject?.getJSONArray("custom_filter_service_id")
                                    if (jsonArray != null && jsonArray?.length()!! > 0) {
                                        if (jsonArray?.length()!! == 1) {

                                            for (i in 0 until jsonArray.length()) {
                                                serviceList?.add(jsonArray?.get(i)?.toString()!!)
                                                // Your code here
                                            }
                                        }
                                    }

                                }
                                /*val uploadCustomerModel = UploadCustomerModel(
                                    false,
                                    false,
                                    "due",
                                    "",
                                    "",
                                    1,
                                    10,
                                    "",
                                    "",
                                    "",
                                    "",
                                    custom_filter,
                                    due_date_from,
                                    due_date_to,
                                    custom_filter_purpose_id,
                                    serviceList,
                                    "",
                                    null,
                                    "",
                                    "",
                                    java.util.ArrayList<String>(),
                                    null,
                                    due_type,
                                    null,
                                    null,
                                    null
                                )

                                intent.putExtra(
                                    "customFilterModel",
                                    gson.toJson(uploadCustomerModel)
                                )*/

                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        } else {
        }
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
        var source: Bitmap? = null
        /*try {
            source = getBitmapFromURL(imageURL)
        } catch (e: Exception) {
            e.printStackTrace()
        }*/
        val notificationBuilder =
            NotificationCompat.Builder(this, "advantage_channel")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.mipmap.noun_gift_797120)
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.app_icon_logo)
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
                .setChannelId("advantage_channel")
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
                .setChannelId("advantage_channel")
                .setContentIntent(pendingIntent)
        }
        var mChannel: NotificationChannel?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = NotificationChannel(
                "advantage_channel",
                "advantage",
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
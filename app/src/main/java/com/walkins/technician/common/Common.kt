package com.example.technician.common

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.walkins.technician.R
import com.walkins.technician.activity.LoginActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jkadvantagandbadsha.model.login.UserModel
import com.jkadvantage.model.vehicleBrandModel.VehicleBrandModel
import com.walkins.technician.custom.BoldButton
import com.jkadvantage.model.vehicleTypeModel.VehicleTypeModel
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import java.lang.IllegalArgumentException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class Common {

    companion object {
        var url: String? = "https://staging-backend.jkadvantage.co.in/api/"

        var urlStaging: String? = "http://qa-picture.jktyrecrm.in/#/sM1I8A"
        var urlRelease: String? = "https://picture.jktyrecrm.in/#/sM1I8A"

        var btn_filled = "btn_primary"
        var btn_not_filled = "btn_secondary"

        var isCalling: Boolean? = false
        private var dialogue: Dialog? = null

        fun timeStampaTimeago(time1: String): String? {
            var time: String = time1
            try {

                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                format.timeZone = TimeZone.getTimeZone("UTC")
                val past = format.parse(time)
                val now = Date()
                val seconds1 = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
                val minutes1 = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
                val hours1 = TimeUnit.MILLISECONDS.toHours(now.time - past.time)
                val days1 = TimeUnit.MILLISECONDS.toDays(now.time - past.time)
                var seconds = Math.abs(seconds1)
                var minutes = Math.abs(minutes1)
                var hours = Math.abs(hours1)
                var days = Math.abs(days1)
                if (seconds < 60) {
                    println("$seconds seconds ago")
                    time = "$seconds seconds ago"
                } else if (minutes < 60) {
                    println("$minutes minutes ago")
                    time = "$minutes minutes ago"
                } else if (hours < 24) {
                    println("$hours hours ago")
                    time = "$hours hours ago"
                } else if (days < 30) {

                    if ("$days".equals("1")) {
                        time = "$days day ago"
                    } else {
                        time = "$days days ago"
                    }
                } else if (days < 365) {
                    var month = days / 30
                    time = "$month month ago"
                } else if (days >= 365) {
                    time = (days / 365).toString() + " year ago"
                } else {
                    println("$days days ago")
                    time = "$days days ago"
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return time
        }

        fun dateDiffrence(past: Date?, now: Date?): String? {

            var time: String? = null
            val now = Date()
            val seconds1 = TimeUnit.MILLISECONDS.toSeconds(now.time - past?.time!!)
            val minutes1 = TimeUnit.MILLISECONDS.toMinutes(now.time - past?.time!!)
            val hours1 = TimeUnit.MILLISECONDS.toHours(now.time - past?.time!!)
            val days1 = TimeUnit.MILLISECONDS.toDays(now.time - past?.time!!)
            var seconds = Math.abs(seconds1)
            var minutes = Math.abs(minutes1)
            var hours = Math.abs(hours1)
            var days = Math.abs(days1)
            if (seconds < 60) {
                println("$seconds seconds")
                time = "$seconds seconds"
            } else if (minutes < 60) {
                println("$minutes minutes")
                time = "$minutes minutes"
            } else if (hours < 24) {
                println("$hours hours")
                time = "$hours hours"
            } else if (days < 30) {

                if ("$days".equals("1")) {
                    time = "$days day"
                } else {
                    time = "$days days"
                }
            } else if (days < 365) {
                var month = days / 30
                time = "$month month"
            } else if (days >= 365) {
                time = (days / 365).toString() + " year"
            } else {
                println("$days days")
                time = "$days days"
            }
            return time
        }

        fun getErrorModel(jsonObject: JSONObject, modelName: String?): Any? {
            val gson: Gson
            val gsonBuilder = GsonBuilder()
            gson = gsonBuilder.create()

            try {
                when (modelName) {

                    "UserModel" -> {
                        val userModel =
                            gson.fromJson(jsonObject.toString(), UserModel::class.java)
                        return userModel
                    }
                    "VehicleBrandModel" -> {
                        val VehicleBrandModel =
                            gson.fromJson(jsonObject.toString(), VehicleBrandModel::class.java)
                        return VehicleBrandModel
                    }

                    else -> {
                        return null
                    }

                }
            } catch (e: java.lang.Exception) {

                e.printStackTrace()
                return null
            }
        }

        fun getModelreturn(
            modelName: String?,
            response: Response<ResponseBody>?,
            i: Int,
            context: Context?
        ): Any? {

            //  val context = MainApplication.applicationContext()

            val gson: Gson
            val gsonBuilder = GsonBuilder()
            gson = gsonBuilder.create()

            return try {

                var resp: String? = null

                if (i == 0) {
                    resp = response?.body()?.string()
                } else {
                    resp = response?.errorBody()?.string()
                }

                val jsonObject = JSONObject(resp)

                if (jsonObject.has("success")) {
                    if (jsonObject.getBoolean("success")) {

                        if (jsonObject.has("warningOrUpdate")) {
                            val jsonObjectForce = jsonObject.getJSONObject("warningOrUpdate")
                            showDialogueForWarning(context!!, jsonObjectForce)
                        }

                        return when (modelName) {


                            "UserModel" -> {
                                val userModel =
                                    gson.fromJson(jsonObject.toString(), UserModel::class.java)
                                return userModel
                            }
                            "VehicleBrandModel" -> {
                                val VehicleBrandModel =
                                    gson.fromJson(
                                        jsonObject.toString(),
                                        VehicleBrandModel::class.java
                                    )
                                return VehicleBrandModel
                            }


                            else -> {
                                return null
                            }

                        }
                    } else {

                        try {


                            if (!isCalling!!) {

                                if (!jsonObject.has("error")) {
                                    return null
                                }
                                isCalling = true
                                val jsonArray: JSONArray = jsonObject.getJSONArray("error")
                                val jsonObjectError: JSONObject = jsonArray.getJSONObject(0)
                                if (jsonObjectError.has("statusCode") && jsonObjectError.getInt(
                                        "statusCode"
                                    ) == 401
                                ) {
                                    val prefManager = PrefManager(context!!)
                                    prefManager.clearAll()
                                    val intent = Intent(context, LoginActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    context.startActivity(intent)

                                } else {
                                    isCalling = false
                                    return getErrorModel(jsonObject, modelName)

                                }


                            } else {
                                return null
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }

                    }
                } else {
                    return null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }

        private fun showDialogueForWarning(
            activity: Context,
            checkStateDataModel: JSONObject
        ) {
            val builder = AlertDialog.Builder(activity).create()
            builder.setCancelable(false)
            val width = LinearLayout.LayoutParams.MATCH_PARENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            builder.window?.setLayout(width, height)

            val root =
                LayoutInflater.from(activity).inflate(R.layout.warning_force_layout, null)

            val btn_cancel = root.findViewById<BoldButton>(R.id.btn_cancel)
            val btn_update = root.findViewById<BoldButton>(R.id.btn_update)
            val tv_message = root.findViewById<TextView>(R.id.tv_message)
            val tv_title = root.findViewById<TextView>(R.id.tv_title)

            if (checkStateDataModel.has("state")) {

                if (checkStateDataModel.getString("state").equals("Force")) {
                    btn_cancel.visibility = View.GONE
                } else {
                    btn_cancel.visibility = View.VISIBLE
                }
            }

            if (checkStateDataModel.has("message")) {
                tv_message.text = checkStateDataModel.getString("message")
            }
            if (checkStateDataModel.has("title")) {
                tv_title.text = checkStateDataModel.getString("title")
            }
            // tv_title.text = checkStateDataModel?.title
            btn_cancel.setOnClickListener { builder.dismiss() }

            btn_update.setOnClickListener {


                val appPackageName =
                    activity.packageName // getPackageName() from Context or Activity object
                try {
                    val intent =
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$appPackageName")
                        )
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    activity.startActivity(intent)
                } catch (anfe: android.content.ActivityNotFoundException) {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    activity.startActivity(intent)
                }

            }
            builder.setView(root)


            builder.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            builder.show()

        }

        fun showShortToast(str: String?, context: Context) {
            Toast.makeText(context, "" + str, Toast.LENGTH_SHORT).show()
        }

        fun showLongToast(str: String, context: Context) {
            Toast.makeText(context, "" + str, Toast.LENGTH_LONG).show()
        }


        fun showLoader(activity: Context) {
            // loadingDialog = LoadingDialog.get(activity).show()


            try {
                if (dialogue != null) {
                    if (dialogue?.isShowing!!) {
                        dialogue?.dismiss()
                    }

                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

            try {
                dialogue = Dialog(activity)
                //  dialogue?.setCancelable(false)
                dialogue?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialogue?.setContentView(R.layout.common_loader)


                dialogue?.show()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        fun hideLoader() {
            try {
                if (dialogue != null && dialogue?.isShowing!!) {
                    dialogue?.dismiss()

                }

            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun capitalizeFirstLetter(str: String?): String {
            return if (str?.length == 0)
                str
            else if (str?.length == 1)
                str.toUpperCase()
            else
                str?.substring(0, 1)?.toUpperCase() + str?.substring(1)?.toLowerCase()
        }

        fun datefrom(date: String): String {
            var displayDate = ""
            try {
                val formatter = SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'")
                val formatterDisplay = SimpleDateFormat("dd-MM-yyyy")
                val dateInString = formatterDisplay.parse(date)
                displayDate = formatter.format(dateInString)

            } catch (e: ParseException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return displayDate
        }

        fun date(date: String): String {
            var displayDate = ""
            try {
                val formatter = SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'")
                val formatterDisplay = SimpleDateFormat("yyyy-MM-dd")
                val dateInString = formatterDisplay.parse(date)
                displayDate = formatter.format(dateInString)
            } catch (e: ParseException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return displayDate
        }

        fun dateTo(date: String): String {
            var displayDate = ""
            try {
                val formatter = SimpleDateFormat("yyyy-MM-dd'T'23:59:59.000'Z'")
                val formatterDisplay = SimpleDateFormat("dd-MM-yyyy")
                val dateInString = formatterDisplay.parse(date)
                displayDate = formatter.format(dateInString)
            } catch (e: ParseException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return displayDate

        }


        fun showDialogue(activity: Activity, message: String) {
            val builder = AlertDialog.Builder(activity).create()
            builder.setCancelable(false)
            val width = LinearLayout.LayoutParams.MATCH_PARENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            builder?.window?.setLayout(width, height)
            builder.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)

            val root = LayoutInflater.from(activity).inflate(R.layout.common_dialogue_layout, null)

            val btnYes = root.findViewById<BoldButton>(R.id.btnOk)
            val tv_message = root.findViewById<TextView>(R.id.tv_message)
            val tv_dialogTitle = root.findViewById<TextView>(R.id.tv_dialogTitle)

            tv_message.text = message
            btnYes.setOnClickListener { builder.dismiss() }
            builder.setView(root)

            builder.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            builder.show()
        }

        fun showDialogueWithTitle(activity: Activity, message: String, title: String) {
            val builder = AlertDialog.Builder(activity).create()
            builder.setCancelable(false)
            val width = LinearLayout.LayoutParams.MATCH_PARENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            builder.window?.setLayout(width, height)
            builder.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)

            val root = LayoutInflater.from(activity).inflate(R.layout.common_dialogue_layout, null)

            val btnYes = root.findViewById<BoldButton>(R.id.btnOk)
            val tv_message = root.findViewById<TextView>(R.id.tv_message)
            val tv_dialogTitle = root.findViewById<TextView>(R.id.tv_dialogTitle)

            tv_dialogTitle.visibility = View.VISIBLE

            tv_message.text = message
            tv_dialogTitle.text = title
            btnYes.setOnClickListener { builder.dismiss() }
            builder.setView(root)

            builder.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            builder.show()
        }

        fun expand(v: View) {
            v.measure(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
            val targtetHeight = v.measuredHeight
            v.layoutParams.height = 0
            v.visibility = View.VISIBLE
            val a: Animation = object : Animation() {
                override fun applyTransformation(
                    interpolatedTime: Float,
                    t: Transformation?
                ) {
                    v.layoutParams.height =
                        if (interpolatedTime == 1f) RecyclerView.LayoutParams.WRAP_CONTENT else (targtetHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }

                override fun willChangeBounds(): Boolean {
                    return true
                }
            }
//        a.setDuration((targtetHeight / v.context.resources.displayMetrics.density).toInt().toLong())
            a.setDuration(300)
            v.startAnimation(a)
        }

        fun collapse(v: View) {
            val initialHeight = v.measuredHeight
            val a: Animation = object : Animation() {
                protected override fun applyTransformation(
                    interpolatedTime: Float,
                    t: Transformation?
                ) {
                    if (interpolatedTime == 1f) {
                        v.visibility = View.GONE



                    } else {
                        v.layoutParams.height =
                            initialHeight - (initialHeight * interpolatedTime).toInt()
                        v.requestLayout()
                    }
                }

                override fun willChangeBounds(): Boolean {
                    return true
                }
            }
//        a.setDuration((initialHeight / v.context.resources.displayMetrics.density).toInt().toLong())
            a.setDuration(300)
            v.startAnimation(a)
        }

    }


}
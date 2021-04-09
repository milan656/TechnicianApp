package com.example.technician.common

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jkadvantagandbadsha.model.login.UserModel
import com.jkadvantage.model.vehicleBrandModel.VehicleBrandModel
import com.walkins.technician.R
import com.walkins.technician.activity.LoginActivity
import com.walkins.technician.custom.BoldButton
import com.walkins.technician.model.login.patternmodel.PatternModel
import com.walkins.technician.model.login.sizemodel.SizeModel
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

class Common {

    companion object {
        //        var url: String? = "https://staging-backend.jkadvantage.co.in/api/"
        var url: String? = "https://stag-tyreservice-backend.trackwalkins.com/"
//        var url: String? = "http://52.3.195.148:5015/api/"

        var urlStaging: String? = "http://qa-picture.jktyrecrm.in/#/sM1I8A"
        var urlRelease: String? = "https://picture.jktyrecrm.in/#/sM1I8A"

        var btn_filled = "btn_primary"
        var btn_not_filled = "btn_secondary"

        var isCalling: Boolean? = false
        private var dialogue: Dialog? = null
        var commonPhotoChooseArr = arrayListOf("Gallery", "Camera")

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

        fun ImageView.setTint(context: Context, @ColorRes colorId: Int) {
            val color = ContextCompat.getColor(context, colorId)
            val colorStateList = ColorStateList.valueOf(color)
            ImageViewCompat.setImageTintList(this, colorStateList)
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
                    "PatternModel" -> {
                        val PatternModel =
                            gson.fromJson(jsonObject.toString(), PatternModel::class.java)
                        return PatternModel
                    }
                    "SizeModel" -> {
                        val SizeModel =
                            gson.fromJson(jsonObject.toString(), SizeModel::class.java)
                        return SizeModel
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
                            "PatternModel" -> {
                                val PatternModel =
                                    gson.fromJson(
                                        jsonObject.toString(),
                                        PatternModel::class.java
                                    )
                                return PatternModel
                            }
                            "SizeModel" -> {
                                val SizeModel =
                                    gson.fromJson(
                                        jsonObject.toString(),
                                        SizeModel::class.java
                                    )
                                return SizeModel
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

        fun getCurrentDateTime(): String {
            var answer: String = ""
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("HH:mm a, dd MMMM yyyy")
                answer = current.format(formatter)
                Log.d("answer", answer)
            } else {
                var date = Date()
                val formatter = SimpleDateFormat("HH:mm a, dd MMMM yyyy")
                answer = formatter.format(date)
                Log.d("answer", answer)
            }
            return answer

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
            v.measure(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            )
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

        @RequiresApi(Build.VERSION_CODES.KITKAT)
        fun getFile(context: Context, uri: Uri?): File? {
            if (uri != null) {
                val path = getPath(context, uri)
                if (path != null && isLocal(path)) {
                    return File(path)
                }
            }
            return null
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @RequiresApi(Build.VERSION_CODES.KITKAT)
        fun getPath(context: Context, uri: Uri): String? {


            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // LocalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split =
                        docId.split((":").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    if ("primary".equals(type, ignoreCase = true)) {
                        return "" + Environment.getExternalStorageDirectory() + "/" + split[1]
                    }

                    // TODO handle non-primary volumes
                } else if (isDownloadsDocument(uri)) {

                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(id)
                    )

                    return getDataColumn(context, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split =
                        docId.split((":").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }

                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])

                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }// MediaProvider
                // DownloadsProvider
                // ExternalStorageProvider
            } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {

                // Return the remote address
                return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                    context,
                    uri,
                    null,
                    null
                )

            } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
                return uri.path
            }// File
            // MediaStore (and general)

            return null
        }

        fun slideToBottom(view: View) {
            val animate = TranslateAnimation(0f, 0f, 0f, view.height.toFloat())
            animate.setDuration(500)
            animate.setFillAfter(true)
            view.startAnimation(animate)

            view.visibility = View.GONE
        }


        fun slideToTop(view: View, view1: View) {
            val animate = TranslateAnimation(0f, 0f, view.height.toFloat(), 0f)
            animate.setDuration(1000)
            animate.setFillAfter(true)
            view.startAnimation(animate)
            view.visibility = View.VISIBLE
            view1.visibility = View.VISIBLE
        }

        fun slideDown(view: View, view1: View?) {
            view.visibility = View.VISIBLE

            if (view1 != null) {
                view1.visibility = View.VISIBLE
            }
            val layoutParams = view.layoutParams
            layoutParams.height = 1
            view.layoutParams = layoutParams
            view.measure(
                View.MeasureSpec.makeMeasureSpec(
                    Resources.getSystem().getDisplayMetrics().widthPixels,
                    View.MeasureSpec.EXACTLY
                ),
                View.MeasureSpec.makeMeasureSpec(
                    0,
                    View.MeasureSpec.UNSPECIFIED
                )
            )
            val height = view.measuredHeight
            val valueAnimator: ValueAnimator = ObjectAnimator.ofInt(1, height)
            valueAnimator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
                override fun onAnimationUpdate(animation: ValueAnimator) {
                    val value = animation.getAnimatedValue() as Int
                    if (height > value) {
                        val layoutParams = view.layoutParams
                        layoutParams.height = value
                        view.layoutParams = layoutParams
                    } else {
                        val layoutParams = view.layoutParams
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                        view.layoutParams = layoutParams
                    }
                }
            })
            valueAnimator.start()
        }


        fun slideUp(view: View) {
            view.post {
                val height = view.height
                val valueAnimator: ValueAnimator = ObjectAnimator.ofInt(height, 0)
                valueAnimator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
                    override fun onAnimationUpdate(animation: ValueAnimator) {
                        val value = animation.getAnimatedValue() as Int
                        if (value > 0) {
                            val layoutParams = view.layoutParams
                            layoutParams.height = value
                            view.layoutParams = layoutParams
                        } else {
                            view.visibility = View.GONE

                        }
                    }
                })
                valueAnimator.start()
            }
        }

        fun slideUp(view: View, view1: View) {
            view.post {
                val height = view.height
                val valueAnimator: ValueAnimator = ObjectAnimator.ofInt(height, 0)
                valueAnimator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
                    override fun onAnimationUpdate(animation: ValueAnimator) {
                        val value = animation.getAnimatedValue() as Int
                        if (value > 0) {
                            val layoutParams = view.layoutParams
                            layoutParams.height = value
                            view.layoutParams = layoutParams
                        } else {
                            view.visibility = View.GONE
                            view1.visibility = View.GONE

                        }
                    }
                })
                valueAnimator.start()
            }
        }

        @Throws(IOException::class)
        fun createFile(context: Context): File {
            // Create an image file name
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir: File? = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            ).apply {
                // Save a file: path for use with ACTION_VIEW intents
//                mCurrentPhotoPath = absolutePath
            }
        }


        fun isLocal(url: String?): Boolean {
            return url != null && !url.startsWith("http://") && !url.startsWith("https://")
        }

        fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }

        fun isGooglePhotosUri(uri: Uri): Boolean {
            return "com.google.android.apps.photos.content" == uri.authority
        }

        fun getDataColumn(
            context: Context, uri: Uri?,
            selection: String?, selectionArgs: Array<String>?
        ): String? {

            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(column)

            try {
                cursor = context.contentResolver.query(
                    uri!!, projection,
                    selection, selectionArgs, null
                )
                if (cursor != null && cursor.moveToFirst()) {
                    val index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(index)
                }
            } finally {
                cursor?.close()
            }
            return null
        }
    }


}
package com.walkins.technician.service

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.SystemClock
import android.util.Log
import com.example.technician.common.PrefManager
import com.example.technician.common.RetrofitCommonClass
import com.google.gson.GsonBuilder
import com.jkadvantage.model.vehicleBrandModel.VehicleBrandModel
import com.walkins.technician.DB.DBClass
import com.walkins.technician.DB.VehicleMakeModelClass
import com.walkins.technician.DB.VehiclePatternModelClass
import com.walkins.technician.DB.VehicleSizeModelClass
import com.walkins.technician.R
import com.walkins.technician.activity.MainActivity
import com.walkins.technician.model.login.patternmodel.PatternModel
import com.walkins.technician.model.login.sizemodel.SizeModel
import com.walkins.technician.networkApi.WarrantyApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class EndlessService : Service() {
    private var prefManager: PrefManager? = null
    private lateinit var mDb: DBClass
    private var wakeLock: PowerManager.WakeLock? = null
    private var isServiceStarted = false
    var context: Context? = null
    private var vehicleBrandModel: VehicleBrandModel? = null

    override fun onBind(intent: Intent): IBinder? {
        Log.e("ENDLESS-SERVICE", "Some component want to bind with the service")
        // We don't provide binding, so return null
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("ENDLESS-SERVICE", "onStartCommand executed with startId: $startId")
        if (intent != null) {
            val action = intent.action
            Log.e("ENDLESS-SERVICE", "using an intent with action $action")
            when (action) {
                Actions.START.name -> startService()
                Actions.STOP.name -> stopService()
                else -> Log.e(
                    "ENDLESS-SERVICE",
                    "This should never happen. No action in the received intent"
                )
            }
        } else {
            Log.e(
                "ENDLESS-SERVICE",
                "with a null intent. It has been probably restarted by the system."
            )
        }
        // by returning this we make sure the service is restarted if the system kills the service
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        prefManager = PrefManager(applicationContext)
        mDb = DBClass.getInstance(applicationContext)
        Log.e("ENDLESS-SERVICE", "The service has been created".toUpperCase())
        val notification = createNotification()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground()
        } else {
            val notif: Notification = Notification()
            startForeground(1, notif)
        }


    }

    @SuppressLint("WrongConstant")
    private fun startMyOwnForeground() {
        val NOTIFICATION_CHANNEL_ID = "channal"
        val channelName = "My Background Service"
        val chan = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                channelName,
                NotificationManager.IMPORTANCE_NONE
            )
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        val notificationBuilder: Notification.Builder =
            Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification: Notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.ic_add_icon)
            .setContentTitle("App is running in background")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(2, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("ENDLESS-SERVICE", "The service has been destroyed".toUpperCase())
//        Toast.makeText(this, "Service destroyed", Toast.LENGTH_SHORT).show()
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        val restartServiceIntent = Intent(applicationContext, EndlessService::class.java).also {
            it.setPackage(packageName)
        };
        val restartServicePendingIntent: PendingIntent =
            PendingIntent.getService(this, 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        applicationContext.getSystemService(Context.ALARM_SERVICE);
        val alarmService: AlarmManager =
            applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager;
        alarmService.set(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime() + 1000,
            restartServicePendingIntent
        );
    }

    private fun startService() {
        if (isServiceStarted) return
        Log.e("ENDLESS-SERVICE", "Starting the foreground service task")
//        Toast.makeText(this, "Service starting its task", Toast.LENGTH_SHORT).show()
        isServiceStarted = true
        setServiceState(this, ServiceState.STARTED)

        // we need this lock so our service gets not affected by Doze Mode
        wakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "EndlessService::lock").apply {
                    acquire()
                }
            }

        // we're starting a loop in a coroutine
        GlobalScope.launch(Dispatchers.IO) {
            while (isServiceStarted) {
                launch(Dispatchers.IO) {
                    fetchVehicleData()

                    fetchPattern()
                    fetchSize()

                    stopService()
                }
                delay(1 * 60 * 1000)
            }
            Log.e("ENDLESS-SERVICE", "End of the loop for the service")
        }
    }

    private fun fetchSize() {
        val warrantyApi = RetrofitCommonClass.createService(WarrantyApi::class.java)

        var call: Call<ResponseBody>? = null
        call = warrantyApi.getVehicleBrand(

        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {

                        val gson = GsonBuilder().create()
                        var sizeModel: SizeModel = gson.fromJson(
                            response.body()?.string(),
                            SizeModel::class.java
                        )
                        Log.e("getmodel00::", "" + vehicleBrandModel)
//                        checkDateTime
                        saveSizeData(sizeModel)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
        })

    }

    private fun fetchPattern() {
        val warrantyApi = RetrofitCommonClass.createService(WarrantyApi::class.java)

        var call: Call<ResponseBody>? = null
        call = warrantyApi.getTyrePattern(
            3

        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {

                        val gson = GsonBuilder().create()
                        var patternModel: PatternModel = gson.fromJson(
                            response?.body()?.string(),
                            PatternModel::class.java
                        )
                        Log.e("getmodel00::", "" + vehicleBrandModel)
//                        checkDateTime
                        savePatternData(patternModel)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
        })

    }

    private fun stopService() {
        Log.e("ENDLESS-SERVICE", "Stopping the foreground service")
//        Toast.makeText(this, "Service stopping", Toast.LENGTH_SHORT).show()
        try {
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                }
            }
            stopForeground(true)
            stopSelf()
        } catch (e: Exception) {
            Log.e("ENDLESS-SERVICE", "Service stopped without being started: ${e.message}")
        }
        isServiceStarted = false
        setServiceState(this, ServiceState.STOPPED)
    }

    private fun fetchVehicleData() {
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmmZ")
        val dfNew = SimpleDateFormat("dd-MM-yyyy HH:mm")
        val gmtTime = dfNew.format(Date())

        prefManager?.setValue("localDBDateTime", gmtTime)
        Log.e("getservicetext", "" + prefManager?.getValue("localDBDateTime"))
        val warrantyApi = RetrofitCommonClass.createService(WarrantyApi::class.java)

        var call: Call<ResponseBody>? = null
        call = warrantyApi.getVehicleBrand(

        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {

                        val gson = GsonBuilder().create()
                        var vehicleBrandModel: VehicleBrandModel = gson.fromJson(
                            response?.body()?.string(),
                            VehicleBrandModel::class.java
                        )
                        Log.e("getmodel00::", "" + vehicleBrandModel)
//                        checkDateTime
                        saveVehicleTypeData(vehicleBrandModel)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
        })

    }

    private fun savePatternData(patternModel: PatternModel) {

        var thread: Thread = Thread {
            if (mDb.patternDaoClass().getAllPattern().size > 0) {
                mDb.patternDaoClass().deleteAll()
            }

            for (i in patternModel.data.indices) {

                var model = patternModel.data.get(i)
                var entity = VehiclePatternModelClass()

                entity.name = if (model.name != null) model.name else ""
                entity.isSelected = false
                mDb.patternDaoClass().savePattern(entity)
            }

            Log.e("response+++", "++++" + mDb.patternDaoClass().getAllPattern().size)
        }

        thread.start()

    }

    private fun saveSizeData(sizeModel: SizeModel) {

        var thread: Thread = Thread {
            if (mDb.sizeDaoClass().getAllSize().size > 0) {
                mDb.sizeDaoClass().deleteAll()
            }

            for (i in sizeModel?.data?.indices!!) {

                var model = sizeModel.data.get(i)
                var entity = VehicleSizeModelClass()

                entity.name = if (model.name != null) model.name else ""
                entity.isSelected = false
                mDb.sizeDaoClass().saveSize(entity)
            }

            Log.e("response+++", "++++" + mDb.sizeDaoClass().getAllSize().size)
        }

        thread.start()

    }

    private fun saveVehicleTypeData(vehicleBrandModel: VehicleBrandModel) {

        var thread: Thread = Thread {
            if (mDb.daoClass().getAllVehicleType().size > 0) {
                mDb.daoClass().deleteAll()
            }

            for (i in vehicleBrandModel?.data?.indices!!) {

                var model = vehicleBrandModel.data.get(i)
                var entity = VehicleMakeModelClass()

                entity.name = if (model.name != null) model.name else ""
                entity.short_number = if (model.short_number != null) model.short_number else ""
                entity.concat = if (model.concat != null) model.concat else ""
                entity.image_url = if (model.image_url != null) model.image_url else ""
                entity.brand_id = if (model.brand_id != null) model.brand_id else ""
                entity.quality = if (model.quality != null) model.quality else ""

                entity.isSelected = false
                mDb.daoClass().saveVehicleType(entity)
            }

            Log.e("response+++", "++++" + mDb.sizeDaoClass().getAllSize().size)
        }

        thread.start()

    }


    private fun createNotification(): Notification {
        val notificationChannelId = "ENDLESS SERVICE CHANNEL"

        // depending on the Android API that we're dealing with we will have
        // to use a specific method to create the notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                notificationChannelId,
                "Endless Service notifications channel",
                NotificationManager.IMPORTANCE_HIGH
            ).let {
                it.description = "Endless Service channel"
                it.enableLights(true)
                it.lightColor = Color.RED
                it.enableVibration(true)
                it.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                it
            }
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val builder: Notification.Builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(
                this,
                notificationChannelId
            ) else Notification.Builder(this)
        val noti = builder
            .setContentTitle("Endless Service")
            .setContentText("This is your favorite endless service working")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setTicker("Ticker text")
            .setPriority(Notification.PRIORITY_HIGH) // for under android 26 compatibility
            .build()


        return noti
    }


}

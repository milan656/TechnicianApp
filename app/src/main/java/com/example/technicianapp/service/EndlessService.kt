package com.example.technicianapp.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import com.example.technician.common.PrefManager
import com.example.technician.common.RetrofitCommonClass
import com.example.technicianapp.DB.DBClass
import com.example.technicianapp.DB.EntityClass
import com.example.technicianapp.R
import com.example.technicianapp.activity.MainActivity
import com.example.technicianapp.networkApi.WarrantyApi
import com.google.gson.Gson
import com.jkadvantage.model.vehicleTypeModel.VehicleTypeModel
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
        val notif: Notification = Notification()
        startForeground(1, notif)


    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("ENDLESS-SERVICE", "The service has been destroyed".toUpperCase())
        Toast.makeText(this, "Service destroyed", Toast.LENGTH_SHORT).show()
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
        Toast.makeText(this, "Service starting its task", Toast.LENGTH_SHORT).show()
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
                }
                delay(1 * 60 * 1000)
            }
            Log.e("ENDLESS-SERVICE", "End of the loop for the service")
        }
    }

    private fun stopService() {
        Log.e("ENDLESS-SERVICE", "Stopping the foreground service")
        Toast.makeText(this, "Service stopping", Toast.LENGTH_SHORT).show()
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
        call = warrantyApi.getVehicleType(prefManager?.getAccessToken()!!)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val gson = Gson()
                        var vehicleTypeModel: VehicleTypeModel = gson.fromJson(
                            response.body().toString(),
                            VehicleTypeModel::class.java
                        )
//                        checkDateTime
                        saveVehicleTypeData(vehicleTypeModel)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
        })

    }

    private fun saveVehicleTypeData(vehicleTypeModel: VehicleTypeModel) {
        if (mDb.daoClass().getAllRecord().size > 0) {
            mDb.daoClass().deleteAll()
        }
        for (i in vehicleTypeModel?.data?.indices!!) {
            var entity = EntityClass()
            entity.vehicle_type_id =
                    vehicleTypeModel.data?.get(i)?.vehicle_type_id!!
            entity.name = vehicleTypeModel.data?.get(i)?.name!!
            entity.image_url = vehicleTypeModel.data?.get(i)?.image_url!!
            entity.type = vehicleTypeModel.data?.get(i)?.type!!

            mDb.daoClass().saveRecord(entity)
        }
        Log.e("response+++", "++++" + mDb.daoClass().getAllRecord().size)
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

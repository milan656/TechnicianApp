package com.walkins.aapkedoorstep.service

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.SystemClock
import android.util.Log
import androidx.core.graphics.drawable.toIcon
import com.example.technician.common.PrefManager
import com.example.technician.common.RetrofitCommonClass
import com.google.gson.GsonBuilder
import com.jkadvantage.model.vehicleBrandModel.VehicleBrandModel
import com.walkins.aapkedoorstep.DB.*
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.activity.MainActivity
import com.walkins.aapkedoorstep.common.TyreKey
import com.walkins.aapkedoorstep.common.dateForWebservice_2
import com.walkins.aapkedoorstep.model.login.ApiUpdatedTimeModel
import com.walkins.aapkedoorstep.model.login.dashboard_model.DashboardServiceListModel
import com.walkins.aapkedoorstep.model.login.issue_list.IssueListModel
import com.walkins.aapkedoorstep.model.login.patternmodel.PatternModel
import com.walkins.aapkedoorstep.model.login.servicelistmodel.ServiceListByDateModel
import com.walkins.aapkedoorstep.model.login.sizemodel.SizeModel
import com.walkins.aapkedoorstep.networkApi.ServiceApi
import com.walkins.aapkedoorstep.networkApi.WarrantyApi
import com.walkins.aapkedoorstep.networkApi.common.CommonApi
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


class BackgroundService : Service() {
    private var prefManager: PrefManager? = null
    private lateinit var mDb: DBClass
    private var wakeLock: PowerManager.WakeLock? = null

    companion object {
        var isServiceStarted = false
    }


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
//        val notification = createNotification()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startMyOwnForeground()
            /*createNotificationChannel()
            val notificationIntent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                this,
                0, notificationIntent, 0
            )
            val notification: Notification = Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentIntent(pendingIntent)
                .build()
            startForeground(1, notification)*/
//            val notif: Notification = Notification()
//            startForeground(2, notif)
//            val notif: Notification = createNotification()
//            startForeground(2, notif)

            lateinit var notificationChannel: NotificationChannel
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as
                    NotificationManager
            lateinit var builder: Notification.Builder
            val channelId = "channel_"
            val description = "Background Notification"
            val intent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.lightColor = Color.BLUE
                notificationChannel.enableVibration(true)
                notificationManager.createNotificationChannel(notificationChannel)
                builder = Notification.Builder(this, channelId)/*.setContentTitle("NOTIFICATION USING " +
                        "KOTLIN")*/.setContentText("App is running in background").setSmallIcon(
                    R.drawable.ic_app_icon_transparent
                ).setLargeIcon(
                    BitmapFactory.decodeResource(
                        this.resources, R.drawable
                            .ic_app_icon_transparent
                    )
                ).setContentIntent(pendingIntent).setAutoCancel(true)
                val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                builder.setSound(alarmSound)
            }
            val noti = builder.build()
            startForeground(2, noti)

//            notificationManager.notify(12345, builder.build())
        } else {
            val notif: Notification = Notification()
            startForeground(1, notif)
        }


    }

    @SuppressLint("WrongConstant")
    private fun startMyOwnForeground() {
        val NOTIFICATION_CHANNEL_ID = "channal"
        val channelName = "Background Service"
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
        val notification: Notification = notificationBuilder.setOngoing(false)
            .setSmallIcon(R.mipmap.ic_app_icon)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)

            .setContentTitle("App is running in background")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()

        startForeground(2, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService()
        Log.e("ENDLESS-SERVICE", "The service has been destroyed".toUpperCase())
//        Toast.makeText(this, "Service destroyed", Toast.LENGTH_SHORT).show()
    }

    override fun onTaskRemoved(rootIntent: Intent) {
//        val restartServiceIntent = Intent(applicationContext, BackgroundService::class.java).also {
//            it.setPackage(packageName)
//        };
//        val restartServicePendingIntent: PendingIntent =
//            PendingIntent.getService(this, 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
//        applicationContext.getSystemService(Context.ALARM_SERVICE);
//        val alarmService: AlarmManager =
//            applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager;
//        alarmService.set(
//            AlarmManager.ELAPSED_REALTIME,
//            SystemClock.elapsedRealtime() + 1000,
//            restartServicePendingIntent
//        );
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

                    fetchTime()

                    if (mDb.ServiceListDashbaordDaoClass().getAll() != null &&
                        mDb.ServiceListDashbaordDaoClass().getAll().size > 0
                    ) {

                    } else {
                        getServiceDashboard()
                    }

                }
                delay(4 * 60 * 1000) // 5 min delay
            }
            Log.e("ENDLESS-SERVICE", "End of the loop for the service")
        }
    }

    private fun getServiceDashboard() {
        val serviceApi = RetrofitCommonClass.createService(ServiceApi::class.java)

        var call: Call<ResponseBody>? = null
        call = serviceApi.getDashboardService("",
            prefManager?.getAccessToken()!!
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val gson = GsonBuilder().create()
                        var dashboardModel: DashboardServiceListModel = gson.fromJson(
                            response.body()?.string(),
                            DashboardServiceListModel::class.java
                        )
                        Log.e("getmodel00::", "" + dashboardModel)
                        saveDashboardService(dashboardModel)

                        if (dashboardModel.data != null && dashboardModel.data.size > 0) {
                            for (i in dashboardModel.data) {
                                Log.e("getpassdata11", "" + i.building_uuid)
                                getServiceList(i.building_uuid, i.date)
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
        })
    }

    private fun getServiceList(buildingUuid: String, date: String) {

        val serviceApi = RetrofitCommonClass.createService(ServiceApi::class.java)

        var call: Call<ResponseBody>? = null
        call = serviceApi.getServiceByDate(dateForWebService(date), buildingUuid,
            prefManager?.getAccessToken()!!
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val gson = GsonBuilder().create()
                        var serviceListModel: ServiceListByDateModel = gson.fromJson(
                            response.body()?.string(),
                            ServiceListByDateModel::class.java
                        )
                        Log.e("getservicelistmodel::", "" + serviceListModel)

                        saveServiceList(serviceListModel, buildingUuid)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
        })
    }

    private fun saveServiceList(serviceListModel: ServiceListByDateModel, buildingUuid: String) {
        var thread: Thread = Thread {
            /* if (mDb.serviceListDaoClass().getAll().size > 0) {
                 mDb.serviceListDaoClass().deleteAll()
             }*/

            if (serviceListModel.data != null && serviceListModel.data.size > 0) {
                for (i in serviceListModel.data) {

                    val entity = ServiceListModelClass()
                    Log.e("getservicelistmodel0::", "" + i.id)
                    entity.serviceId = i.id.toString()
                    entity.uuid = i.uuid
                    entity.color = i.color
                    entity.color_code = i.color_code
                    entity.status = i.status
                    entity.regNumber = i.regNumber
                    entity.service_user_name = i.service_user_name
                    entity.service_user_mobile = i.service_user_mobile
                    entity.make = i.make
                    entity.model = i.model
                    entity.make_id = i.make_id
                    entity.model_id = i.model_id
                    entity.model_image = i.model_image
                    entity.building_uuid = buildingUuid
                    if (i.service != null && i.service.size > 0) {
                        entity.service = i.service
                    }
                    entity.image = i.image
                    entity.buildingName = i.buildingName
                    entity.address = i.address

                    if (i.comment_id != null && i.comment_id.size > 0) {
                        entity.comment_id = i.comment_id
                    }

                    mDb.serviceListDaoClass().save(entity)
                }

            }
            Log.e("response+++", "++++" + mDb.serviceListDaoClass().getAll().size)
        }

        thread.start()

    }

    private fun fetchIssueList() {
        val commonApi = RetrofitCommonClass.createService(CommonApi::class.java)

        var call: Call<ResponseBody>? = null
        call = commonApi.getListOfIssue(
            prefManager?.getAccessToken()!!
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {

                        val gson = GsonBuilder().create()
                        val issueListModel: IssueListModel = gson.fromJson(
                            response.body()?.string(),
                            IssueListModel::class.java
                        )
                        Log.e("getmodel00::", "" + issueListModel)
//                        checkDateTime
                        saveListOfIssue(issueListModel)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
        })
    }

    private fun fetchSize() {
        val warrantyApi = RetrofitCommonClass.createService(WarrantyApi::class.java)

        var call: Call<ResponseBody>? = null
        call = warrantyApi.getVehicleTyreSize(
            prefManager?.getAccessToken()!!

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
                        Log.e("getmodel00::", "" + sizeModel)
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


    public fun stopService() {
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

    private fun fetchTime() {
        val warrantyApi = RetrofitCommonClass.createService(WarrantyApi::class.java)

        var call: Call<ResponseBody>? = null
        call = warrantyApi.getUpdatedTime(
            prefManager?.getAccessToken()!!

        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {

                        val gson = GsonBuilder().create()
                        val apiUpdatedTimeModel: ApiUpdatedTimeModel = gson.fromJson(
                            response.body()?.string(),
                            ApiUpdatedTimeModel::class.java
                        )

                        Log.e("getmodel00::", "" + apiUpdatedTimeModel)

                        Log.e("getmodel00::", "" + prefManager?.getValue(TyreKey.lastTyreBrandUpdatedDate))
                        Log.e("getmodel00::", "" + prefManager?.getValue(TyreKey.lastSizeUpdatedDate))
                        Log.e("getmodel00::", "" + prefManager?.getValue(TyreKey.lastPatternUpdatedDate))

                        if (prefManager?.getValue(TyreKey.lastTyreBrandUpdatedDate) != null &&
                            !prefManager?.getValue(TyreKey.lastTyreBrandUpdatedDate).equals("")
                        ) {
                            if (!prefManager?.getValue(TyreKey.lastTyreBrandUpdatedDate).equals(apiUpdatedTimeModel.data.lastTyreBrandUpdatedDate)) {
                                prefManager?.setValue(TyreKey.lastTyreBrandUpdatedDate, apiUpdatedTimeModel.data.lastTyreBrandUpdatedDate)
                                fetchVehicleData()
                            }
                        } else {
                            prefManager?.setValue(TyreKey.lastTyreBrandUpdatedDate, apiUpdatedTimeModel.data.lastTyreBrandUpdatedDate)
                            fetchVehicleData()
                        }
                        if (prefManager?.getValue(TyreKey.lastPatternUpdatedDate) != null &&
                            !prefManager?.getValue(TyreKey.lastPatternUpdatedDate).equals("")
                        ) {
                            if (!prefManager?.getValue(TyreKey.lastPatternUpdatedDate).equals(apiUpdatedTimeModel.data.lastPatternUpdatedDate)) {
                                prefManager?.setValue(TyreKey.lastPatternUpdatedDate, apiUpdatedTimeModel.data.lastPatternUpdatedDate)
                                fetchPattern()
                            }
                        } else {
                            prefManager?.setValue(TyreKey.lastPatternUpdatedDate, apiUpdatedTimeModel.data.lastPatternUpdatedDate)
                            fetchPattern()
                        }
                        if (prefManager?.getValue(TyreKey.lastSizeUpdatedDate) != null &&
                            !prefManager?.getValue(TyreKey.lastSizeUpdatedDate).equals("")
                        ) {
                            if (!prefManager?.getValue(TyreKey.lastSizeUpdatedDate).equals(apiUpdatedTimeModel.data.lastSizeUpdatedDate)) {
                                prefManager?.setValue(TyreKey.lastSizeUpdatedDate, apiUpdatedTimeModel.data.lastSizeUpdatedDate)
                                fetchSize()
                            }
                        } else {
                            prefManager?.setValue(TyreKey.lastSizeUpdatedDate, apiUpdatedTimeModel.data.lastSizeUpdatedDate)
                            fetchSize()
                        }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
        })

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
            prefManager?.getAccessToken()!!
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {

                        val gson = GsonBuilder().create()
                        var vehicleBrandModel: VehicleBrandModel = gson.fromJson(
                            response.body()?.string(),
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

    private fun fetchPattern() {
        val warrantyApi = RetrofitCommonClass.createService(WarrantyApi::class.java)

        var call: Call<ResponseBody>? = null
        call = warrantyApi.getTyrePattern(
            prefManager?.getAccessToken()!!

        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {

                        val gson = GsonBuilder().create()
                        val patternModel: PatternModel = gson.fromJson(
                            response.body()?.string(),
                            PatternModel::class.java
                        )
                        Log.e("getmodel00::", "" + patternModel)
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

    private fun savePatternData(patternModel: PatternModel) {

        var thread: Thread = Thread {
            if (mDb.patternDaoClass().getAllPattern().size > 0) {
                mDb.patternDaoClass().deleteAll()
            }

            for (i in patternModel.data.indices) {

                var entity = VehiclePatternModelClass()

                entity.name =
                    if (patternModel.data?.get(i)?.name != null) patternModel.data?.get(i)?.name else ""
                entity.patternId = patternModel.data?.get(i)?.patternId
                if (patternModel.data.get(i).brand_id != null && !patternModel.data.get(i).brand_id.equals("")) {
                    entity.brand_id = patternModel.data.get(i).brand_id.toInt()
                } else {
                    entity.brand_id = -1
                }
                entity.isSelected = false
                mDb.patternDaoClass().savePattern(entity)
            }

            Log.e("response+++", "++++" + mDb.patternDaoClass().getAllPattern())
        }

        thread.start()

    }

    private fun saveStaticPatternData() {

        var thread: Thread = Thread {
            if (mDb.patternDaoClass().getAllPattern().size > 0) {
                mDb.patternDaoClass().deleteAll()
            }

            for (i in 0..10) {

                var entity = VehiclePatternModelClass()

                entity.name =
                    "101H546 45" + i
                entity.patternId = 45 + i
                entity.isSelected = false
                mDb.patternDaoClass().savePattern(entity)
            }

            Log.e("response+++", "++++" + mDb.patternDaoClass().getAllPattern())
        }

        thread.start()

    }

    private fun saveSizeData(sizeModel: SizeModel) {

        var thread: Thread = Thread {
            if (mDb.sizeDaoClass().getAllSize().size > 0) {
                mDb.sizeDaoClass().deleteAll()
            }

            for (i in sizeModel.data.indices) {

                var entity = VehicleSizeModelClass()

                entity.name =
                    if (sizeModel.data?.get(i)?.name != null) sizeModel.data.get(i).name else ""
                entity.sizeId = sizeModel.data.get(i).sizeId
                entity.make_id = sizeModel.data.get(i).make_id
                entity.model_id = sizeModel.data.get(i).model_id
                entity.isSelected = false
                mDb.sizeDaoClass().saveSize(entity)
            }

            Log.e("response+++", "++++" + mDb.sizeDaoClass().getAllSize())
        }

        thread.start()

    }

    fun saveStaticSize() {
        val thread = Thread {
            if (mDb.sizeDaoClass().getAllSize().size > 0) {
                mDb.sizeDaoClass().deleteAll()
            }

            for (i in 0..10) {

                var entity = VehicleSizeModelClass()

                entity.name =
                    "120H 785" + i
                entity.sizeId = 655 + i
                entity.isSelected = false
                mDb.sizeDaoClass().saveSize(entity)
            }
        }
        thread.start()
    }


    private fun saveVehicleTypeData(vehicleBrandModel: VehicleBrandModel) {

        var thread: Thread = Thread {
            if (mDb.daoClass().getAllVehicleType().size > 0) {
                mDb.daoClass().deleteAll()
            }

            if (vehicleBrandModel.data != null && vehicleBrandModel.data.size!! > 0) {
                for (i in vehicleBrandModel.data?.indices!!) {

                    var model = vehicleBrandModel.data.get(i)
                    var entity = VehicleMakeModelClass()

                    entity.name = if (model.name != null) model.name else ""
                    entity.brand_id = model.id?.toString()
                    entity.short_number = if (model.short_number != null) model.short_number else ""
                    entity.concat = if (model.concat != null) model.concat else ""
                    entity.image_url = if (model.image_url != null) model.image_url else ""
                    entity.quality = if (model.quality != null) model.quality else ""

                    entity.isSelected = false
                    mDb.daoClass().saveVehicleType(entity)
                }

            }
            Log.e("response+++", "++++" + mDb.sizeDaoClass().getAllSize().size)
        }

        thread.start()

    }

    private fun saveListOfIssue(issueListModel: IssueListModel) {

        val thread: Thread = Thread {
            if (mDb.issueListDaoClass().getAllIssue().size > 0) {
                mDb.issueListDaoClass().deleteAll()
            }

            if (issueListModel.data != null && issueListModel.data.size > 0) {
                for (i in issueListModel.data.indices) {

                    val model = issueListModel.data.get(i)
                    val entity = IssueListModelClass()

                    entity.name = if (model.name != null) model.name else ""
                    entity.issueId = model.id

                    entity.isSelected = false
                    mDb.issueListDaoClass().saveIssue(entity)
                }

            }
            Log.e("response+++", "++++" + mDb.issueListDaoClass().getAllIssue().size)
        }

        thread.start()

    }

    fun saveStaticVehicleMake() {
        val thread = Thread {

            if (mDb.daoClass().getAllVehicleType() != null) {
                if (mDb.daoClass().getAllVehicleType().size > 0) {
                    mDb.daoClass().deleteAll()
                }
            }
            for (i in 0..10) {
//                    var model = vehicleBrandModel.data.get(i)
                var entity = VehicleMakeModelClass()

                entity.name = "Test name" + i
                entity.short_number = ""
                entity.concat =
                    "https://homepages.cae.wisc.edu/~ece533/images/serrano.png"
                entity.image_url =
                    "https://homepages.cae.wisc.edu/~ece533/images/serrano.png"
                entity.brand_id = ""
                entity.quality = ""

                entity.isSelected = false
                mDb.daoClass().saveVehicleType(entity)
            }
        }
        thread.start()
    }

    private fun createNotification(): Notification {
        val notificationChannelId = "SERVICE CHANNEL"

        // depending on the Android API that we're dealing with we will have
        // to use a specific method to create the notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                notificationChannelId,
                "Service notifications channel",
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
//            .setContentTitle("Endless Service")
            .setContentText("App is Running in Background")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_app_logo)
//            .setTicker("Ticker text")
            .setPriority(Notification.PRIORITY_HIGH) // for under android 26 compatibility
            .build()


        return noti
    }

    private fun saveDashboardService(dashboardServiceListModel: DashboardServiceListModel) {

        val thread: Thread = Thread {
            if (mDb.ServiceListDashbaordDaoClass().getAll().size > 0) {
                mDb.ServiceListDashbaordDaoClass().deleteAll()
            }
            if (mDb.serviceListDaoClass().getAll().size > 0) {
                mDb.serviceListDaoClass().deleteAll()
            }

            if (dashboardServiceListModel.data != null && dashboardServiceListModel.data.size > 0) {
                for (i in dashboardServiceListModel.data) {

                    var entity = ServiceDashboardModelClass()

                    val dateString = i.date
                    Log.e("getdatefrom", "" + dateString)
                    val sdf = SimpleDateFormat("yyyy-MM-dd")
                    val date = sdf.parse(dateString)

                    val startDate = date.time

                    entity.buildingnamearea = i.building_name + ", " + i.area
                    entity.address = i.address
                    entity.date = i.date
                    entity.buildinguuid = i.building_uuid
                    entity.dateformated = i.date_formated
                    entity.openjobs = i.open_jobs.toInt()
                    entity.completedjobs = i.complete_jobs.toInt()
                    entity.skippedjobs = i.skip_jobs.toInt()
                    entity.totaljobs = i.total_jobs.toInt()
                    entity.startdate = startDate
                    entity.updatedate = startDate

                    val mDateFormat = SimpleDateFormat("dd MMMM yy")
                    val mToday = mDateFormat.format(Date())
                    val date_ = mDateFormat.format(Date(entity.dateformated)).toString()
                    Log.e("getdatefor", "" + mToday + " " + date_)

                    if (mToday.equals(date_)) {
                        mDb.ServiceListDashbaordDaoClass().save(entity)
                    }
                }

            }
            Log.e("response+++", "++++" + mDb.ServiceListDashbaordDaoClass().getAll().size)
        }

        thread.start()

    }

    fun dateForWebService(date: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val formatterDisplay = SimpleDateFormat("yyyy-MM-dd")
        val dateInString = formatterDisplay.parse(date)
        val displayDate = formatter.format(dateInString)
        return displayDate
    }
}

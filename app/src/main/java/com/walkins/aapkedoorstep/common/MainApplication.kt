package com.example.technician.common

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

class MainApplication : MultiDexApplication() {

    init {
        instance = this
    }

    companion object {
        private var instance: MainApplication? = null

        //        var firebaseCrashlytics: FirebaseCrashlytics? = null
        fun applicationContext(): Context {
            return instance?.applicationContext!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        try {
            MultiDex.install(this);
        } catch (e: Exception) {
            e.printStackTrace()
        }
//        firebaseCrashlytics = FirebaseCrashlytics.getInstance()
//        firebaseCrashlytics?.setCrashlyticsCollectionEnabled(true)
//        firebaseCrashlytics?.log("crashreport Log")
//        firebaseCrashlytics?.sendUnsentReports()

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityResumed(activity: Activity) {

            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {

//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                    Log.e("getaction", "" + activity.isDestroyed)
//                    if (isMyServiceRunning(BackgroundService::class.java)) {
//                        (baseContext as BackgroundService).stopForeground(true)
//                    }
//
//                }

            }

        })
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}
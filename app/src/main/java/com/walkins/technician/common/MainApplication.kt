package com.example.technician.common

import android.content.Context
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

        val context: Context = applicationContext()
    }
}
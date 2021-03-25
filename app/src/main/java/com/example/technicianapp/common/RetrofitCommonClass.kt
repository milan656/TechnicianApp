package com.example.walkinslatestapp.common

import android.os.Build
import android.text.TextUtils
import androidx.core.content.pm.PackageInfoCompat
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitCommonClass {

    companion object CommonRetrofit {
        val context = MainApplication.applicationContext()

        val manager = context.packageManager

        private fun getDeviceName(): String? {
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            return if (model.startsWith(manufacturer)) {
                model.capitalize()
            } else manufacturer.capitalize() + " " + model
        }

        val info = manager.getPackageInfo(context.packageName, 0)
        var versionCode = PackageInfoCompat.getLongVersionCode(info).toInt()

        private fun getOsName(): String? {

            var androidOS = Build.VERSION.RELEASE
            val fields = Build.VERSION_CODES::class.java.fields
            for (field in fields) {
                androidOS = field.name
            }
            return androidOS
        }

        private val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)

            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request =
                        chain.request().newBuilder().addHeader("app", "advantage")
                            .addHeader("device_type", "android")
                            .addHeader("apk_version", "" + versionCode)
                            .addHeader("mobile_model", "" + getDeviceName())
                            .addHeader("mobile_os_version", "" + getOsName())
                            .addHeader("Content-Type", "application/json").build()
                    return chain.proceed(request)

                }

            })
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        //http://192.168.4.105/api/v1/crm-points/reports/all
        // http://192.168.4.106:5000/api/v1/auth/login
        // http://staging-backend.jkadvantage.co.in:5000/api/v1/auth/login
        // http://192.168.4.113:5000/api/v1/
        //https://backend.jktyrecrm.in/api/v1/
        private val retrofit = Retrofit.Builder()
            .baseUrl(Common.url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        fun <S> createService(serviceClass: Class<S>): S {
            return retrofit.create(serviceClass)
        }
    }
}
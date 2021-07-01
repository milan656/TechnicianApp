package com.walkins.aapkedoorstep.common

import com.example.technician.common.Common
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitBuilder {
    companion object {
        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(Common.url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }

        fun <S> createService(serviceClass: Class<S>): S {
            return retrofit.create(serviceClass)
        }

        private val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)

            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request =
                        chain.request().newBuilder()
//                            .addHeader("app", "technician")
//                            .addHeader("device_type", "android")
//                            .addHeader("apk_version", "" + versionCode)
//                            .addHeader("mobile_model", "" + getDeviceName())
//                            .addHeader("mobile_os_version", "" + getOsName())
                            .addHeader("Content-Type", "application/json").build()
                    return chain.proceed(request)

                }

            })
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }
}

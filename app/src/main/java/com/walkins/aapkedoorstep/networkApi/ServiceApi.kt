package com.walkins.aapkedoorstep.networkApi

import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ServiceApi {

    @POST("v1/user/add-service-detail")
    fun addService(
        @Body jsonObject: JsonObject,
        @Header("Authorization") authorization: String
    ): Call<ResponseBody>

    @POST("v1/user/update-skip-service")
    fun updateService(
        @Body jsonObject: JsonObject,
        @Header("Authorization") authorization: String
    ): Call<ResponseBody>

    @GET("v1/user/get-service-count-by-date")
    fun getDashboardService(
        @Query("date") date: String,
        @Header("Authorization") authorization: String
    ): Call<ResponseBody>

    @GET("v1/user/get-service-by-date")
    fun getServiceByDate(
        @Query("date") date: String,@Query("building_id") building_id:String,
        @Header("Authorization") authorization: String
    ): Call<ResponseBody>

    @POST("v1/user/get-technician-report-detail")
    fun getReportService(
        @Body jsonObject: JsonObject,
        @Header("Authorization") authorization: String
    ): Call<ResponseBody>

}
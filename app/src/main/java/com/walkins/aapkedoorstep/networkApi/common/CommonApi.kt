package com.walkins.aapkedoorstep.networkApi.common

import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface CommonApi {

    @GET("v1/tyrepushpull/get-list-of-issue")
    fun getListOfIssue(@Header("Authorization") authorizationToke: String): Call<ResponseBody>

    @GET("v1/tyrepushpull/get-comment")
    fun getCommentList(@Header("Authorization") authorizationToke: String): Call<ResponseBody>

    @GET("v1/notification/get-notification")
    fun getNotificationList(@Header("Authorization") authorizationToke: String): Call<ResponseBody>

    @GET("v1/notification/get-unread-notification-count")
    fun getNotificationCount(@Header("Authorization") authorizationToke: String): Call<ResponseBody>

    @GET("v1/tyrepushpull/get-building")
    fun getBuildingList(@Header("Authorization") authorizationToke: String): Call<ResponseBody>

    @GET("v1/tyrepushpull/get-service")
    fun getgetService(@Header("Authorization") authorizationToke: String): Call<ResponseBody>

    @GET("v1/user/get-user-info")
    fun getUserInfo(@Header("Authorization") authorizationToke: String): Call<ResponseBody>

    @GET("/api/v1/user/logout-from-all")
    fun callLogoutFromAll(@Header("Authorization") authorizationToke: String): Call<ResponseBody>

    @POST("/api/v1/notification/save-token")
    fun callApiSaveToken(
        @Body jsonObject: JsonObject,
        @Header("Authorization") authorizationToke: String): Call<ResponseBody>

    @POST("v1/user/get-vehicle-service-by-id")
    fun getgetServiceById(
        @Body jsonObject: JsonObject,
        @Header("Authorization") authorizationToke: String
    ): Call<ResponseBody>
}
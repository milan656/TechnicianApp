package com.walkins.aapkedoorstep.networkApi.common

import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface CommonApi {

    @GET("v1/tyrepushpull/get-list-of-issue")
    suspend fun getListOfIssue(@Header("Authorization") authorizationToke: String): Response<ResponseBody>

    @GET("v1/tyrepushpull/get-comment")
    suspend fun getCommentList(@Header("Authorization") authorizationToke: String): Response<ResponseBody>

    @GET("v1/notification/get-notification")
    fun getNotificationList(@Header("Authorization") authorizationToke: String): Call<ResponseBody>

    @GET("v1/notification/get-unread-notification-count")
    suspend fun getNotificationCount(@Header("Authorization") authorizationToke: String): Response<ResponseBody>

    @GET("v1/tyrepushpull/get-building")
    fun getBuildingList(@Header("Authorization") authorizationToke: String): Call<ResponseBody>

    @GET("v1/tyrepushpull/get-service")
    suspend fun getgetService(@Header("Authorization") authorizationToke: String): Response<ResponseBody>

    @GET("v1/user/get-user-info")
    suspend fun getUserInfo(@Header("Authorization") authorizationToke: String): Response<ResponseBody>

    @GET("/api/v1/user/logout-from-all")
    fun callLogoutFromAll(@Header("Authorization") authorizationToke: String): Call<ResponseBody>

    @POST("/api/v1/notification/save-token")
    suspend fun callApiSaveToken(
        @Body jsonObject: JsonObject,
        @Header("Authorization") authorizationToke: String): Response<ResponseBody>

    @POST("v1/user/get-vehicle-service-by-id")
    suspend fun getgetServiceById(
        @Body jsonObject: JsonObject,
        @Header("Authorization") authorizationToke: String
    ): Response<ResponseBody>
}
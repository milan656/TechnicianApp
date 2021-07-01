package com.walkins.aapkedoorstep.networkApi.login

import com.google.gson.JsonObject
import com.jkadvantagandbadsha.model.login.UserModel
import com.walkins.aapkedoorstep.model.login.otp.OtpModel
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface LoginApi {

    @POST("v1/auth/login")
    @FormUrlEncoded
    suspend fun loginUser(
        @Field("username") userId: String, @Field("password") password: String, @Field("grant_type") grantType: String,
        @Header("Authorization") authorizationToke: String,
    ): Response<ResponseBody>

    @POST("v1/tyrepushpull/send-otp")
    fun callApiSendOTP(
        @Body jsonObject: JsonObject,
    ): Call<ResponseBody>

    @POST("v1/tyrepushpull/send-otp")
    suspend fun loginUserTwo(
        @Body jsonObject: JsonObject,

        /*@Field("username") userId: String, @Field("password") password: String, @Field("grant_type") grantType: String,
        @Header("Authorization") authorizationToke: String,
        @Header("apk_version") versionCode: Int,
        @Header("mobile_model") deviceName: String?,
        @Header("mobile_os_version") androidOS: String*/
    ): Response<ResponseBody>


    @POST("v1/auth/login")
    @FormUrlEncoded
    fun refreshToken(
        @Header("Authorization") authorizationToke: String,
        @Field("grant_type") grantType: String,
        @Field("refresh_token") refreshToken: String,
    ): Call<UserModel>

    @Multipart
    @POST("v1/tyrepushpull/upload-image")
    fun uploadFile(
        @Part file: MultipartBody.Part,
        @Header("Authorization") authorizationToke: String,
//        @Header("Content-Type") content:String,
        @Query("type") type: String,
    ): Call<ResponseBody>


}
package com.example.technicianapp.networkApi.login

import com.jkadvantagandbadsha.model.login.UserModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface LoginApi {

    @POST("v1/auth/login")
    @FormUrlEncoded
    fun loginUser(
        @Field("username") userId: String, @Field("password") password: String, @Field("grant_type") grantType: String,
        @Header("Authorization") authorizationToke: String
    ): Call<UserModel>

    @POST
    @FormUrlEncoded
    fun loginUserTwo(
        @Url url: String?,
        @Field("username") userId: String, @Field("password") password: String, @Field("grant_type") grantType: String,
        @Header("Authorization") authorizationToke: String,
        @Header("apk_version") versionCode: Int,
        @Header("mobile_model") deviceName: String?,
        @Header("mobile_os_version") androidOS: String
    ): Call<UserModel>


    @POST("v1/auth/login")
    @FormUrlEncoded
    fun refreshToken(
        @Header("Authorization") authorizationToke: String,
        @Field("grant_type") grantType: String,
        @Field("refresh_token") refreshToken: String
    ): Call<UserModel>

}
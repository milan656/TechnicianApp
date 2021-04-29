package com.walkins.technician.networkApi.common

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface CommonApi {

    @GET("v1/tyrepushpull/get-list-of-issue")
    fun getListOfIssue(@Header("Authorization") authorizationToke: String): Call<ResponseBody>

    @GET("v1/tyrepushpull/get-service")
    fun getgetService(@Header("Authorization") authorizationToke: String): Call<ResponseBody>
}
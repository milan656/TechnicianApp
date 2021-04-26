package com.walkins.technician.networkApi

import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ServiceApi {

    @POST("v1/tyrepushpull/add-service-detail")
    fun addService(
        @Body jsonObject: JsonObject,
        @Header("Authorization") authorization: String
    ): Call<ResponseBody>
}
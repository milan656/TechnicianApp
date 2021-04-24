package com.walkins.technician.networkApi

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MakeModelApi {

//    https://stag-tyreservice-backend.trackwalkins.com/get-vehicle-make
//    https://stag-tyreservice-backend.trackwalkins.com/get-vehicle-model?id=2

    @GET("v1/tyrepushpull/get-vehicle-make")
    fun getVehicleMake(@Header("Authorization") authorization: String): Call<ResponseBody>

    @GET("v1/tyrepushpull/get-vehicle-model")
    fun getVehicleModel(@Query("id") id: Int,
                        @Header("Authorization") authorization: String): Call<ResponseBody>

}
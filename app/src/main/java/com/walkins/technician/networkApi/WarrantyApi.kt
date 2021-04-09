package com.walkins.technician.networkApi

import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface WarrantyApi {

    @POST("v2/warranty/secondary-points/send-otp")
    fun sendOtp(
        @Body jsonObject: JsonObject, @Header("Authorization") authorization: String
    ): Call<ResponseBody>

    @POST("v2/warranty/secondary-points/verify-otp")
    fun verifyOtp(
        @Body jsonObject: JsonObject, @Header("Authorization") authorization: String
    ): Call<ResponseBody>

    @POST("v1/warranty/secondary-points/find-customer")
    fun getCustomerDetails(
        @Body jsonObject: JsonObject, @Header("Authorization") authorization: String
    ): Call<ResponseBody>

    @GET("get-tyre-brand")
    fun getVehicleBrand(
        /*@Query("vehicle_type_id") category: String, @Header("Authorization") authorization: String*/
    ): Call<ResponseBody>

    @GET("get-tyre-size")
    fun getVehicleTyreSize(
        @Query("model_id") model_id: Int, @Query("make_id") make_id: Int
    ): Call<ResponseBody>

    //    https://stag-tyreservice-backend.trackwalkins.com/get-tyre-pattern?id=2
    @GET("get-tyre-pattern")
    fun getTyrePattern(@Query("brand_id") id: Int): Call<ResponseBody>

    @GET("v1/warranty/vehicle/get-vehicle-type-brand-model")
    fun getVehicleTypeBrand(
        @Query("vehicle_type_brand_id") vehicle_type_brand_id: String,
        @Header("Authorization") authorization: String
    ): Call<ResponseBody>

    @GET("v1/warranty/vehicle/get-vehicle-type-brand-model-variation")
    fun getVehicleVarient(
        @Query("vehicle_type_brand_model_id") vehicle_type_brand_model_id: String,
        @Header("Authorization") authorization: String
    ): Call<ResponseBody>

    @GET("v1/warranty/vehicle/get-vehicle-sku-id")
    fun getSkuId(
        @Query("vehicle_type") vehicle_type: String,
        @Query("searchquery") searchquery: String,
        @Header(
            "Authorization"
        ) authorization: String
    ): Call<ResponseBody>

    @GET("v1/warranty/state-city/get-state")
    fun getAllState(
        @Header("Authorization") authorization: String
    ): Call<ResponseBody>

    @GET("v1/warranty/state-city/get-state-city")
    fun getAllCity(
        @Query("state_id") state_id: String, @Header("Authorization") authorization: String
    ): Call<ResponseBody>

    @GET("v2/warranty/vehicle/get-vehicle-type")
    fun getVehicleTypev2(
        @Header("Authorization") authorization: String
    ): Call<ResponseBody>

    @GET("v2/warranty/vehicle/get-vehicle-type")
    fun getVehicleType(
        @Header("Authorization") authorization: String
    ): Call<ResponseBody>


    @POST("v3/warranty/secondary-points/registration")
    fun callWarrantyRegistrationApiTwo(
        @Body jsonObject: JsonObject, @Header("Authorization") authorization: String
    ): Call<ResponseBody>

    @GET("/api/v1/warranty/vehicle/get-vehicle-top-sku-id")
    fun callApiSkuDetailList(
        @Query("vehicle_type_id") vehicle_type_id: String,
        @Query("vehicle_type_brand_id") vehicle_type_brand_id: String,
        @Query("vehicle_type_brand_model_id") vehicle_type_brand_model_id: String,
        @Query("vehicle_category") vehicle_category: String,
        @Header("Authorization") authorization: String
    ): Call<ResponseBody>

    @GET("/api/v1/warranty/vehicle/get-vehicle-top-sku-id")
    fun callApiSkuDetailListnew(
        @Query("vehicle_type_id") vehicle_type_id: String,
        @Query("vehicle_type_brand_id") vehicle_type_brand_id: String,
        @Query("vehicle_category") vehicle_category: String,
        @Header("Authorization") authorization: String
    ): Call<ResponseBody>

}


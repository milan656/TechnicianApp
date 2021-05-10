package com.walkins.aapkedoorstep.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.technician.common.Common
import com.example.technician.common.RetrofitCommonClass
import com.jkadvantage.model.vehicleBrandModel.VehicleBrandModel
import com.walkins.aapkedoorstep.model.login.patternmodel.PatternModel
import com.walkins.aapkedoorstep.model.login.sizemodel.SizeModel
import com.walkins.aapkedoorstep.networkApi.WarrantyApi
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class WarrantyRepository {


    var otpVerifyRepository: WarrantyRepository? = null

    lateinit var otpApi: WarrantyApi

    constructor() {
        otpApi = RetrofitCommonClass.createService(WarrantyApi::class.java)
    }


    companion object

    fun getInstance(): WarrantyRepository {
        if (otpVerifyRepository == null) {
            otpVerifyRepository = WarrantyRepository()
        }
        return otpVerifyRepository as WarrantyRepository
    }


    fun getVehicleBrand(
        vehicle_type_id: String,
        access_token: String, context: Context
    ): MutableLiveData<VehicleBrandModel> {
        var otpData = MutableLiveData<VehicleBrandModel>()
        otpApi.getVehicleBrand(access_token)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) = if (response.isSuccessful) {
                    otpData.value = Common?.getModelreturn(
                        "VehicleBrandModel",
                        response,
                        0,
                        context
                    ) as VehicleBrandModel?


                } else {
                    try {
                        otpData.value = Common?.getModelreturn(
                            "VehicleBrandModel",
                            response,
                            1,
                            context
                        ) as VehicleBrandModel?
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                }
            })
        return otpData
    }

    fun getVehiclePattern(
        brand_id: Int,
        access_token: String,
        context: Context
    ): MutableLiveData<PatternModel> {
        var otpData = MutableLiveData<PatternModel>()
        otpApi.getTyrePattern(brand_id,access_token)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) = if (response.isSuccessful) {
                    otpData.value = Common?.getModelreturn(
                        "PatternModel",
                        response,
                        0,
                        context
                    ) as PatternModel?


                } else {
                    try {
                        otpData.value = Common?.getModelreturn(
                            "PatternModel",
                            response,
                            1,
                            context
                        ) as PatternModel?
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                }
            })
        return otpData
    }

    fun getVehicleSize(
        model_id: Int,
        make_id: Int,
        access_token: String,
        context: Context
    ): MutableLiveData<SizeModel> {
        var otpData = MutableLiveData<SizeModel>()
        otpApi.getVehicleTyreSize(/*vehicle_type_id, access_token*/model_id, make_id,access_token)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) = if (response.isSuccessful) {
                    otpData.value = Common?.getModelreturn(
                        "SizeModel",
                        response,
                        0,
                        context
                    ) as SizeModel?


                } else {
                    try {
                        otpData.value = Common?.getModelreturn(
                            "SizeModel",
                            response,
                            1,
                            context
                        ) as SizeModel?
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                }
            })
        return otpData
    }


}
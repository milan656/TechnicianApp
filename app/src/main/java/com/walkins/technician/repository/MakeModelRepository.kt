package com.walkins.technician.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.technician.common.Common
import com.example.technician.common.RetrofitCommonClass
import com.jkadvantage.model.vehicleBrandModel.VehicleBrandModel
import com.walkins.technician.model.login.makemodel.VehicleMakeModel
import com.walkins.technician.model.login.makemodel.VehicleModel
import com.walkins.technician.model.login.patternmodel.PatternModel
import com.walkins.technician.model.login.sizemodel.SizeModel
import com.walkins.technician.networkApi.MakeModelApi
import com.walkins.technician.networkApi.WarrantyApi
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class MakeModelRepository {


    var otpVerifyRepository: MakeModelRepository? = null

    lateinit var otpApi: MakeModelApi

    constructor() {
        otpApi = RetrofitCommonClass.createService(MakeModelApi::class.java)
    }


    companion object

    fun getInstance(): MakeModelRepository {
        if (otpVerifyRepository == null) {
            otpVerifyRepository = MakeModelRepository()
        }
        return otpVerifyRepository as MakeModelRepository
    }


    fun getVehicleMake(
        context: Context,
        accessToken:String
    ): MutableLiveData<VehicleMakeModel> {
        var otpData = MutableLiveData<VehicleMakeModel>()
        otpApi.getVehicleMake(accessToken)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) = if (response.isSuccessful) {
                    otpData.value = Common?.getModelreturn(
                        "VehicleMakeModel",
                        response,
                        0,
                        context
                    ) as VehicleMakeModel?


                } else {
                    try {
                        otpData.value = Common?.getModelreturn(
                            "VehicleMakeModel",
                            response,
                            1,
                            context
                        ) as VehicleMakeModel?
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

    fun getVehicleModel(
        makeId: Int,
        accessToken: String,
        context: Context
    ): MutableLiveData<VehicleModel> {
        var otpData = MutableLiveData<VehicleModel>()
        otpApi.getVehicleModel(makeId,accessToken)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) = if (response.isSuccessful) {
                    otpData.value = Common?.getModelreturn(
                        "VehicleModel",
                        response,
                        0,
                        context
                    ) as VehicleModel?


                } else {
                    try {
                        otpData.value = Common?.getModelreturn(
                            "VehicleModel",
                            response,
                            1,
                            context
                        ) as VehicleModel?
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
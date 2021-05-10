package com.walkins.aapkedoorstep.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.technician.common.Common
import com.example.technician.common.RetrofitCommonClass
import com.jkadvantage.model.vehicleBrandModel.VehicleBrandModel
import com.walkins.aapkedoorstep.model.login.building.BuildingListModel
import com.walkins.aapkedoorstep.model.login.makemodel.VehicleMakeModel
import com.walkins.aapkedoorstep.model.login.makemodel.VehicleModel
import com.walkins.aapkedoorstep.model.login.patternmodel.PatternModel
import com.walkins.aapkedoorstep.model.login.sizemodel.SizeModel
import com.walkins.aapkedoorstep.networkApi.MakeModelApi
import com.walkins.aapkedoorstep.networkApi.WarrantyApi
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class MakeModelRepository {


    var otpVerifyRepository: MakeModelRepository? = null

    lateinit var makeModelApi: MakeModelApi

    constructor() {
        makeModelApi = RetrofitCommonClass.createService(MakeModelApi::class.java)
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
        makeModelApi.getVehicleMake(accessToken)
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
 fun getBuilding(
        context: Context,
        accessToken:String
    ): MutableLiveData<BuildingListModel> {
        var otpData = MutableLiveData<BuildingListModel>()
        makeModelApi.getBuilding(accessToken)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) = if (response.isSuccessful) {
                    otpData.value = Common.getModelreturn(
                        "BuildingListModel",
                        response,
                        0,
                        context
                    ) as BuildingListModel?


                } else {
                    try {
                        otpData.value = Common.getModelreturn(
                            "BuildingListModel",
                            response,
                            1,
                            context
                        ) as BuildingListModel?
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
        makeModelApi.getVehicleModel(makeId,accessToken)
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
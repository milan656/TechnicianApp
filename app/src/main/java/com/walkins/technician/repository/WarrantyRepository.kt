package com.walkins.technician.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.technician.common.Common
import com.example.technician.common.RetrofitCommonClass
import com.jkadvantage.model.vehicleBrandModel.VehicleBrandModel
import com.walkins.technician.networkApi.WarrantyApi
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
        otpApi.getVehicleBrand(vehicle_type_id, access_token)
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


}
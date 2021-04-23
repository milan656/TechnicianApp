package com.walkins.technician.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.technician.common.Common
import com.example.technician.common.RetrofitCommonClass
import com.google.gson.JsonObject
import com.jkadvantagandbadsha.model.login.UserModel
import com.walkins.technician.model.login.servicemodel.AddServiceModel
import com.walkins.technician.networkApi.common.CommonApi
import com.walkins.technician.networkApi.serviceApi
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ServiceRepo {

    var serviceRepo: ServiceRepo? = null

    lateinit var serviceApi: serviceApi

    constructor() {
        serviceApi = RetrofitCommonClass.createService(serviceApi::class.java)
    }

    companion object

    fun getInstance(): ServiceRepo {
        if (serviceRepo == null) {
            serviceRepo = ServiceRepo()
        }
        return serviceRepo as ServiceRepo
    }

    fun addService(
        jsonObject: JsonObject,
        access_token: String, context: Context
    ): MutableLiveData<AddServiceModel> {
        var servicedata = MutableLiveData<AddServiceModel>()

        var addEdit: Call<ResponseBody>?=serviceApi.addService(jsonObject, access_token)

        addEdit?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>
            ) = if (response.isSuccessful) {
                servicedata.value = Common?.getModelreturn(
                    "AddServiceModel",
                    response,
                    0,
                    context
                ) as AddServiceModel?

            } else {
                try {
                    servicedata.value = Common?.getModelreturn(
                        "AddServiceModel",
                        response,
                        1,
                        context
                    ) as AddServiceModel?
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("getCipResponse", "" + t.cause + " " + t.message)
//                servicedata.value = null
            }
        })
        return servicedata
    }
}
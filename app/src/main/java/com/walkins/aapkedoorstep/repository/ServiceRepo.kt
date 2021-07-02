package com.walkins.aapkedoorstep.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.technician.common.Common
import com.example.technician.common.RetrofitCommonClass
import com.google.gson.JsonObject
import com.walkins.aapkedoorstep.common.RetrofitBuilder
import com.walkins.aapkedoorstep.model.login.ReportServiceModel
import com.walkins.aapkedoorstep.model.login.dashboard_model.DashboardServiceListModel
import com.walkins.aapkedoorstep.model.login.servicelistmodel.ServiceListByDateModel
import com.walkins.aapkedoorstep.model.login.servicemodel.AddServiceModel
import com.walkins.aapkedoorstep.networkApi.ServiceApi
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class ServiceRepo {

    var serviceRepo: ServiceRepo? = null

    lateinit var serviceApi: ServiceApi

    constructor() {
        serviceApi = RetrofitCommonClass.createService(ServiceApi::class.java)
    }

    companion object

    fun getInstance(): ServiceRepo {
        if (serviceRepo == null) {
            serviceRepo = ServiceRepo()
        }
        return serviceRepo as ServiceRepo
    }

    suspend fun addService(
        jsonObject: JsonObject,
        access_token: String, context: Context,
    ): Response<ResponseBody> = RetrofitBuilder.createService(ServiceApi::class.java).addService(jsonObject, access_token)

/*
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
*/

    suspend fun UpdateService(
        jsonObject: JsonObject,
        access_token: String, context: Context,
    ):Response<ResponseBody>  = RetrofitBuilder.createService(ServiceApi::class.java).updateService(jsonObject, access_token)

    /*fun UpdateService(
        jsonObject: JsonObject,
        access_token: String, context: Context,
    ): MutableLiveData<AddServiceModel> {
        var servicedata = MutableLiveData<AddServiceModel>()

        var addEdit: Call<ResponseBody>? = serviceApi.updateService(jsonObject, access_token)

        addEdit?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>,
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
    }*/

    fun getDashboardService(
        date: String,
        access_token: String, context: Context,
    ): MutableLiveData<DashboardServiceListModel> {
        val servicedata = MutableLiveData<DashboardServiceListModel>()

        val addEdit: Call<ResponseBody>? = serviceApi.getDashboardService(date, access_token)

        addEdit?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>,
            ) = if (response.isSuccessful) {
                servicedata.value = Common.getModelreturn(
                    "DashboardServiceListModel",
                    response,
                    0,
                    context
                ) as DashboardServiceListModel?

            } else {
                try {
                    servicedata.value = Common.getModelreturn(
                        "DashboardServiceListModel",
                        response,
                        1,
                        context
                    ) as DashboardServiceListModel?
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

    suspend fun getServiceByDate(
        date: String,
        building_id: String,
        access_token: String, context: Context,
    ):Response<ResponseBody> = RetrofitBuilder.createService(ServiceApi::class.java).getServiceByDate(date, building_id, access_token)

    /*fun getServiceByDate(
        date: String,
        building_id: String,
        access_token: String, context: Context,
    ): MutableLiveData<ServiceListByDateModel> {
        val servicedata = MutableLiveData<ServiceListByDateModel>()

        val addEdit: Call<ResponseBody>? = serviceApi.getServiceByDate(date, building_id, access_token)

        addEdit?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>,
            ) = if (response.isSuccessful) {
                servicedata.value = Common.getModelreturn(
                    "ServiceListByDateModel",
                    response,
                    0,
                    context
                ) as ServiceListByDateModel?

            } else {
                try {
                    servicedata.value = Common.getModelreturn(
                        "ServiceListByDateModel",
                        response,
                        1,
                        context
                    ) as ServiceListByDateModel?
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
    }*/

    fun callApiReportService(
        jsonObject: JsonObject,
        access_token: String, context: Context,
    ): MutableLiveData<ReportServiceModel> {
        val servicedata = MutableLiveData<ReportServiceModel>()

        val addEdit: Call<ResponseBody>? = serviceApi.getReportService(jsonObject, access_token)

        addEdit?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>,
            ) = if (response.isSuccessful) {
                servicedata.value = Common.getModelreturn(
                    "ReportServiceModel",
                    response,
                    0,
                    context
                ) as ReportServiceModel?

            } else {
                try {
                    servicedata.value = Common.getModelreturn(
                        "ReportServiceModel",
                        response,
                        1,
                        context
                    ) as ReportServiceModel?
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
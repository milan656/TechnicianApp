package com.walkins.technician.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.technician.common.Common
import com.example.technician.common.RetrofitCommonClass
import com.google.gson.JsonObject
import com.walkins.technician.model.login.comment.CommentListModel
import com.walkins.technician.model.login.issue_list.IssueListModel
import com.walkins.technician.model.login.makemodel.VehicleMakeModel
import com.walkins.technician.model.login.service.ServiceModel
import com.walkins.technician.model.login.servicemodel.servicedata.ServiceDataByIdModel
import com.walkins.technician.networkApi.MakeModelApi
import com.walkins.technician.networkApi.common.CommonApi
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class CommonRepo {

    var commonRepo: CommonRepo? = null

    lateinit var commonApi: CommonApi

    constructor() {
        commonApi = RetrofitCommonClass.createService(CommonApi::class.java)
    }

    companion object

    fun getInstance(): CommonRepo {
        if (commonRepo == null) {
            commonRepo = CommonRepo()
        }
        return commonRepo as CommonRepo
    }

    fun getListOfIssue(
        context: Context,
        accessToken:String

    ): MutableLiveData<IssueListModel> {
        var otpData = MutableLiveData<IssueListModel>()
        commonApi.getListOfIssue(accessToken)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) = if (response.isSuccessful) {
                    otpData.value = Common.getModelreturn(
                        "IssueListModel",
                        response,
                        0,
                        context
                    ) as IssueListModel?
                } else {
                    try {
                        otpData.value = Common.getModelreturn(
                            "IssueListModel",
                            response,
                            1,
                            context
                        ) as IssueListModel?
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

    fun getService(
        context: Context,
        accessToken:String

    ): MutableLiveData<ServiceModel> {
        var otpData = MutableLiveData<ServiceModel>()
        commonApi.getgetService(accessToken)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) = if (response.isSuccessful) {
                    otpData.value = Common.getModelreturn(
                        "ServiceModel",
                        response,
                        0,
                        context
                    ) as ServiceModel?
                } else {
                    try {
                        otpData.value = Common.getModelreturn(
                            "ServiceModel",
                            response,
                            1,
                            context
                        ) as ServiceModel?
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

    fun getCommentList(
        context: Context,
        accessToken:String

    ): MutableLiveData<CommentListModel> {
        var otpData = MutableLiveData<CommentListModel>()
        commonApi.getCommentList(accessToken)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) = if (response.isSuccessful) {
                    otpData.value = Common.getModelreturn(
                        "CommentListModel",
                        response,
                        0,
                        context
                    ) as CommentListModel?
                } else {
                    try {
                        otpData.value = Common.getModelreturn(
                            "CommentListModel",
                            response,
                            1,
                            context
                        ) as CommentListModel?
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

    fun getServiceById(
        jsonObject: JsonObject,
        context: Context,
        accessToken:String

    ): MutableLiveData<ServiceDataByIdModel> {
        var otpData = MutableLiveData<ServiceDataByIdModel>()
        commonApi.getgetServiceById(jsonObject,accessToken)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) = if (response.isSuccessful) {
                    otpData.value = Common.getModelreturn(
                        "ServiceDataByIdModel",
                        response,
                        0,
                        context
                    ) as ServiceDataByIdModel?
                } else {
                    try {
                        otpData.value = Common.getModelreturn(
                            "ServiceDataByIdModel",
                            response,
                            1,
                            context
                        ) as ServiceDataByIdModel?
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
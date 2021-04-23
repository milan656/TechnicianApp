package com.walkins.technician.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.technician.common.Common
import com.example.technician.common.RetrofitCommonClass
import com.walkins.technician.model.login.issue_list.IssueListModel
import com.walkins.technician.model.login.makemodel.VehicleMakeModel
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
        context: Context
    ): MutableLiveData<IssueListModel> {
        var otpData = MutableLiveData<IssueListModel>()
        commonApi.getListOfIssue(/*vehicle_type_id, access_token*/)
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
}
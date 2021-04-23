package com.walkins.technician.networkApi.common

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface CommonApi {

    @GET("get-list-of-issue")
    fun getListOfIssue(): Call<ResponseBody>
}
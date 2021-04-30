package com.walkins.technician.model.login.servicelistmodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jkadvantage.model.error.ErrorModel

data class ServiceListByDateModel(
    @SerializedName("success")
    @Expose
    var success: Boolean,
    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("data")
    @Expose
    var data: List<ServiceListByDateData>,
    @SerializedName("error")
    @Expose
    val error: ArrayList<ErrorModel>
)
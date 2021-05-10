package com.walkins.aapkedoorstep.model.login.service

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jkadvantage.model.error.ErrorModel

data class
ServiceModel(
    @SerializedName("success")
    @Expose
    var success: Boolean,
    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("data")
    @Expose
    var data: List<ServiceModelData>,
    @SerializedName("error")
    @Expose
    val error: ArrayList<ErrorModel>
)
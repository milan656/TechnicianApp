package com.walkins.technician.model.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jkadvantage.model.error.ErrorModel

data class ReportServiceModel(
    @SerializedName("success")
    @Expose
    val success: Boolean,
    @SerializedName("message")
    @Expose
    val message: String,
    @SerializedName("data")
    @Expose
    val data: ReportserviceData,
    @SerializedName("error")
    @Expose
    val error: ArrayList<ErrorModel>
)
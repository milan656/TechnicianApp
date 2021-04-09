package com.walkins.technician.model.login.sizemodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jkadvantage.model.error.ErrorModel

data class SizeModel(
    @SerializedName("success")
    @Expose
    var success: Boolean,
    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("data")
    @Expose
    var data: List<SizeData>,
    @SerializedName("error")
    @Expose
    val error: ArrayList<ErrorModel>,
)
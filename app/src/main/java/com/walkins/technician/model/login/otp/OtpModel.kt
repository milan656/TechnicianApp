package com.walkins.technician.model.login.otp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jkadvantage.model.error.ErrorModel

data class OtpModel(

    @SerializedName("message")
    @Expose
    val message: String,
    @SerializedName("data")
    @Expose
    val data: Data?,
    @SerializedName("success")
    @Expose
    val success: Boolean,
    @SerializedName("error")
    @Expose
    val error: ArrayList<ErrorModel>
)
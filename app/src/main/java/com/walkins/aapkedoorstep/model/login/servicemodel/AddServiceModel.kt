package com.walkins.aapkedoorstep.model.login.servicemodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jkadvantage.model.error.ErrorModel

data class AddServiceModel(
    @SerializedName("success")
    @Expose
    var success: Boolean,
    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("error")
    @Expose
    val error: ArrayList<ErrorModel>
)
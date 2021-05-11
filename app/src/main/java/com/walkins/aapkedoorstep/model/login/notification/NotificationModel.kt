package com.walkins.aapkedoorstep.model.login.notification

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jkadvantage.model.error.ErrorModel

data class NotificationModel(
    @SerializedName("success")
    @Expose
    val success: Boolean,
    @SerializedName("message")
    @Expose
    val message: String,
    @SerializedName("data")
    @Expose
    val data: NotificationData,
    @SerializedName("error")
    @Expose
    val error: ArrayList<ErrorModel>
)
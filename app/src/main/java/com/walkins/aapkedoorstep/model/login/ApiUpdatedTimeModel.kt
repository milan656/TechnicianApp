package com.walkins.aapkedoorstep.model.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ApiUpdatedTimeModel(
    @SerializedName("success")
    @Expose
    val success:Boolean,
@SerializedName("message")
@Expose
val message:String,
@SerializedName("data")
@Expose
val data:UpdateTimeData
)
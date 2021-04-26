package com.walkins.technician.model.login.makemodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jkadvantage.model.error.ErrorModel

data class VehicleModel(
    @SerializedName("success")
    @Expose
    var success: Boolean,
    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("data")
    @Expose
    var data: List<VehicleModelData>,
    @SerializedName("error")
    @Expose
    val error: ArrayList<ErrorModel>

)
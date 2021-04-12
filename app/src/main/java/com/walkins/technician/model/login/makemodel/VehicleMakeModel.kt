package com.walkins.technician.model.login.makemodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VehicleMakeModel(
    @SerializedName("success")
    @Expose
    var success: Boolean,
    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("data")
    @Expose
    var data: List<VehicleMakeData>

)
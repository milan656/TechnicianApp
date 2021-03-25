package com.jkadvantage.model.vehicleTypeModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jkadvantage.model.error.ErrorModel

data class VehicleTypeModel(
    @SerializedName("success")
    @Expose
    var success: Boolean,
    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("data")
    @Expose
    var data: List<Data>,
    @SerializedName("error")
    @Expose
    val error: ArrayList<ErrorModel>
)
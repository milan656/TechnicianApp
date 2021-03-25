package com.jkadvantage.model.vehicleTypeModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jkadvantage.model.error.ErrorModel

data class  VehicleTypeModel(
    @SerializedName("data")
    @Expose
    var data: List<Data>?,
    @SerializedName("code")
    @Expose
    var code: Int,
    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("status")
    @Expose
    var status: Int,
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int,
    @SerializedName("success")
    @Expose
    var success: Boolean,

    @SerializedName("error")
    @Expose
    val error: ArrayList<ErrorModel>
)
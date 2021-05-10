package com.jkadvantage.model.vehicleBrandModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jkadvantage.model.error.ErrorModel

data class VehicleBrandModel(
    @SerializedName("data")
    @Expose
    val data: List<Data>?,
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
    @SerializedName("error")
    @Expose
    val error: ArrayList<ErrorModel>,
    @SerializedName("success")
    @Expose
    var success: Boolean
)
package com.walkins.technician.model.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.walkins.technician.model.login.servicelistmodel.ServiceListData

data class ReportServiceData(

    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("make")
    @Expose
    val make: String,
    @SerializedName("color")
    @Expose
    val color: String,
    @SerializedName("model")
    @Expose
    val model: String,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("service")
    @Expose
    val service: List<ServiceListData>,
    @SerializedName("make_image")
    @Expose
    val makeImage: String,
    @SerializedName("reg_number")
    @Expose
    val regNumber: String,
    @SerializedName("model_image")
    @Expose
    val modelImage: String,
    @SerializedName("actual_service_date")
    @Expose
    val actualServiceDate: String
)
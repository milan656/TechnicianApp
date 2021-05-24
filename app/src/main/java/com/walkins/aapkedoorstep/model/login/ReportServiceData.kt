package com.walkins.aapkedoorstep.model.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.walkins.aapkedoorstep.model.login.servicelistmodel.ServiceListData

data class ReportServiceData(

    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("make")
    @Expose
    val make: String,
    @SerializedName("uuid")
    @Expose
    val uuid: String,
    @SerializedName("address")
    @Expose
    val address: String,
    @SerializedName("area")
    @Expose
    val area: String,
    @SerializedName("color")
    @Expose
    val color: String,
    @SerializedName("model")
    @Expose
    val model: String,
    @SerializedName("color_code")
    @Expose
    val color_code: String,
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
    val actualServiceDate: String,
    @SerializedName("service_scheduled_date")
    @Expose
    val service_scheduled_date: String,
    @SerializedName("comment_id")
    @Expose
    val comment_id: List<Int>

)
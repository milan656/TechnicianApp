package com.walkins.technician.model.login.servicelistmodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ServiceListByDateData(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("uuid")
    @Expose
    val uuid: String,
    @SerializedName("color")
    @Expose
    val color: String,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("reg_number")
    @Expose
    val regNumber: String,
    @SerializedName("make")
    @Expose
    val make: String,
    @SerializedName("model")
    @Expose
    val model: String,
    @SerializedName("service")
    @Expose
    val service: List<ServiceListData>,
    @SerializedName("image")
    @Expose
    val image: String,
    @SerializedName("building_name")
    @Expose
    val buildingName: String,
    @SerializedName("address")
    @Expose
    val address: String
)
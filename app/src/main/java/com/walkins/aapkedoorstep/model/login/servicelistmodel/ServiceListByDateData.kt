package com.walkins.aapkedoorstep.model.login.servicelistmodel

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
    @SerializedName("color_code")
    @Expose
    var color_code: String,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("reg_number")
    @Expose
    val regNumber: String,
    @SerializedName("service_user_name")
    @Expose
    val service_user_name: String,
    @SerializedName("service_user_mobile")
    @Expose
    val service_user_mobile: String,
    @SerializedName("make")
    @Expose
    val make: String,
    @SerializedName("model")
    @Expose
    val model: String,
    @SerializedName("make_id")
    @Expose
    val make_id: Int,
    @SerializedName("model_id")
    @Expose
    val model_id: Int,
    @SerializedName("model_image")
    @Expose
    val model_image: String,
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
    val address: String,
    @SerializedName("comment_id")
    @Expose
    val comment_id: List<Int>
)
package com.walkins.technician.model.login.building

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jkadvantage.model.error.ErrorModel

data class BuildingListModel(
    @SerializedName("success")
    @Expose
    var success: Boolean,
    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("data")
    @Expose
    var data: List<BuildingListData>,
    @SerializedName("error")
    @Expose
    val error: ArrayList<ErrorModel>
)
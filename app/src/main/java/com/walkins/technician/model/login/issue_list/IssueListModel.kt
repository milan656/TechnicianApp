package com.walkins.technician.model.login.issue_list

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jkadvantage.model.error.ErrorModel
import com.walkins.technician.model.login.makemodel.VehicleMakeData

data class IssueListModel(
    @SerializedName("success")
    @Expose
    var success: Boolean,
    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("data")
    @Expose
    var data: List<IssueModelData>,
    @SerializedName("error")
    @Expose
    val error: ArrayList<ErrorModel>
)
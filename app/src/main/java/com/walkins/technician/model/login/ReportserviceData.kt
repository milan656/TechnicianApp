package com.walkins.technician.model.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReportserviceData(
    @SerializedName("complete")
    @Expose
    val complete: String,
    @SerializedName("skip")
    @Expose
    val skip: String,
    @SerializedName("elements")
    @Expose
    val serviceData: ArrayList<ReportServiceData>
)
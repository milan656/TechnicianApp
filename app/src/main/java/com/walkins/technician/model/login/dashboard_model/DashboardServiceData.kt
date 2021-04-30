package com.walkins.technician.model.login.dashboard_model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DashboardServiceData(
    @SerializedName("date_formated")
    @Expose
    var date_formated: String,
    @SerializedName("date")
    @Expose
    var date: String,
    @SerializedName("building_name")
    @Expose
    var building_name: String,
    @SerializedName("address")
    @Expose
    var address: String,
    @SerializedName("open_jobs")
    @Expose
    var open_jobs: String,
    @SerializedName("complete_jobs")
    @Expose
    var complete_jobs: String,
    @SerializedName("skip_jobs")
    @Expose
    var skip_jobs: String
)
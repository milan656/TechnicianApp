package com.walkins.technician.model.login.makemodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VehicleModelData(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("name")
    @Expose
    var name: String
)
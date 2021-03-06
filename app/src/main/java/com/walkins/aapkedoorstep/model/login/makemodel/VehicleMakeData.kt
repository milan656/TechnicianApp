package com.walkins.aapkedoorstep.model.login.makemodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VehicleMakeData(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("name")
    @Expose
    var name: String
)
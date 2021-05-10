package com.walkins.aapkedoorstep.model.login.service

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ServiceModelData(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("image")
    @Expose
    var image: String,
    var isSelected: Boolean = false
)
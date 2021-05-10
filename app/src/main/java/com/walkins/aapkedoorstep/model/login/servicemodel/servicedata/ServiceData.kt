package com.walkins.aapkedoorstep.model.login.servicemodel.servicedata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ServiceData(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("image")
    @Expose
    val image: String
)
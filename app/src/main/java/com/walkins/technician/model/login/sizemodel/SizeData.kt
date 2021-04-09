package com.walkins.technician.model.login.sizemodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SizeData(

    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("size")
    @Expose
    var name: String,
    var isSelected: Boolean = false
)
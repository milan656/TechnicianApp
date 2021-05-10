package com.walkins.aapkedoorstep.model.login.sizemodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SizeData(

    @SerializedName("id")
    @Expose
    var sizeId: Int,
    @SerializedName("size")
    @Expose
    var name: String,
    var isSelected: Boolean = false,
    var isRFSelected: Boolean = false,
    var isLRSelected: Boolean = false,
    var isRRSelected: Boolean = false,
)
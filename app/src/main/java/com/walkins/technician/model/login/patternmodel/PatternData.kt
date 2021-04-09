package com.walkins.technician.model.login.patternmodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PatternData(

    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("name")
    @Expose
    var name: String,
    var isSelected: Boolean = false
)
package com.walkins.aapkedoorstep.model.login.patternmodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PatternData(

    @SerializedName("id")
    @Expose
    var patternId: Int,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("brand_id")
    @Expose
    var brand_id: String,
    var isSelected: Boolean = false,
    var isRFSelected: Boolean = false,
    var isLRSelected: Boolean = false,
    var isRRSelected: Boolean = false,
)
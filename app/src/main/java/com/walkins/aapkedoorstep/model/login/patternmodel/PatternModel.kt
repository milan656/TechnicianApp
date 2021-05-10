package com.walkins.aapkedoorstep.model.login.patternmodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jkadvantage.model.error.ErrorModel

data class PatternModel(
    @SerializedName("success")
    @Expose
    var success: Boolean,
    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("data")
    @Expose
    var data: List<PatternData>,
    @SerializedName("error")
    @Expose
    val error: ArrayList<ErrorModel>
)
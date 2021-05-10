package com.jkadvantage.model.notification.saveToken

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jkadvantage.model.error.ErrorModel

data class SaveTokenModel(
    val message: String,
    val success: Boolean,
    @SerializedName("error")
    @Expose
    val error: ArrayList<ErrorModel>
)
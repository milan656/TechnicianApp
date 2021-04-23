package com.jkadvantage.model.customerIntraction.uploadimage

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UploadImageModel(

    @SerializedName("success")
    @Expose
    val success: Boolean,
    @SerializedName("message")
    @Expose
    val message: String,
    @SerializedName("data")
    @Expose
    val data: UploadImageData
)
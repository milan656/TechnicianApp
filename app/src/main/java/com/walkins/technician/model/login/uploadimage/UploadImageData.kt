package com.jkadvantage.model.customerIntraction.uploadimage

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UploadImageData(
    @SerializedName("imageUrl")
    @Expose
    val imageUrl: String
)
package com.jkadvantage.model.vehicleTypeModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data(

    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("type")
    @Expose
    var type: String,
    @SerializedName("vehicle_type_id")
    @Expose
    var vehicleTypeId: String,
    @SerializedName("image_url")
    @Expose
    var imageUrl: String
)
package com.jkadvantage.model.vehicleTypeModel

import com.google.gson.annotations.Expose

data class Data(

    @Expose
    val image_url: String,
    @Expose
    val name: String,
    @Expose
    val vehicle_type_id: String,
    @Expose
    val type: String,
    @Expose
    var isSelected: Boolean
)
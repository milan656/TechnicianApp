package com.jkadvantage.model.vehicleBrandModel

data class Data(
//    var id: Int,
    val brand_id: String,
    val image_url: String,
    val name: String,
    val short_number: String,
    var isSelected: Boolean,
    var quality: String,
    var vehicle_type: String,
    var concat: String,
    var isRFSelected: Boolean = false,
    var isLRSelected: Boolean = false,
    var isRRSelected: Boolean = false,


    )
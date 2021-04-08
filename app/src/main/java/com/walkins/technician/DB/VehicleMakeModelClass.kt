package com.walkins.technician.DB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class VehicleMakeModelClass {

    @PrimaryKey(autoGenerate = true)
    var Id: Int = 0

    @ColumnInfo(name = "name")
    var name: String = ""

    @ColumnInfo(name = "short_number")
    var short_number: String = ""

    @ColumnInfo(name = "quality")
    var quality: String = ""

    @ColumnInfo(name = "brand_id")
    var brand_id: String = ""

    @ColumnInfo(name = "vehicle_type")
    var vehicle_type: String = ""

    @ColumnInfo(name = "image_url")
    var image_url: String = ""

    @ColumnInfo(name = "isSelected")
    var isSelected: Boolean = false

    @ColumnInfo(name = "isRFSelected")
    var isRFSelected: Boolean = false

    @ColumnInfo(name = "isLRSelected")
    var isLRSelected: Boolean = false

    @ColumnInfo(name = "isRRSelected")
    var isRRSelected: Boolean = false
}
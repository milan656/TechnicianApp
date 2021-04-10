package com.walkins.technician.DB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class VehicleSizeModelClass {

    @PrimaryKey(autoGenerate = true)
    var Id: Int = 0

    @ColumnInfo(name = "sizeId")
    var sizeId: Int = 0

    @ColumnInfo(name = "name")
    var name: String? = ""

    @ColumnInfo(name = "isSelected")
    var isSelected: Boolean = false

    @ColumnInfo(name = "isRFSelected")
    var isRFSelected: Boolean = false

    @ColumnInfo(name = "isLRSelected")
    var isLRSelected: Boolean = false

    @ColumnInfo(name = "isRRSelected")
    var isRRSelected: Boolean = false
}
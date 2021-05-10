package com.walkins.aapkedoorstep.DB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehiclesize")
class VehicleSizeModelClass {

    @PrimaryKey(autoGenerate = true)
    var Id: Int = 0
    var name: String? = ""
    var sizeId: Int? = 0
    var isSelected: Boolean = false
    var isRFSelected: Boolean = false
    var isLRSelected: Boolean = false
    var isRRSelected: Boolean = false
}
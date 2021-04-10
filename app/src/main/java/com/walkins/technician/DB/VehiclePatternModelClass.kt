package com.walkins.technician.DB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class VehiclePatternModelClass {

    @PrimaryKey(autoGenerate = true)
    var Id: Int = 0

    @ColumnInfo(name = "patternId")
    var patternId: Int = 0

    @ColumnInfo(name = "name")
    var name: String? = ""

    @ColumnInfo(name = "image_url")
    var image_url: String? = ""

    @ColumnInfo(name = "concat")
    var concat: String? = ""

    @ColumnInfo(name = "isSelected")
    var isSelected: Boolean = false

    @ColumnInfo(name = "isRFSelected")
    var isRFSelected: Boolean = false

    @ColumnInfo(name = "isLRSelected")
    var isLRSelected: Boolean = false

    @ColumnInfo(name = "isRRSelected")
    var isRRSelected: Boolean = false
}
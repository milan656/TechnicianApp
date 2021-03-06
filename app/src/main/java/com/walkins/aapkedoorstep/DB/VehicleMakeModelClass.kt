package com.walkins.aapkedoorstep.DB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehiclemake")
class VehicleMakeModelClass {

    @PrimaryKey(autoGenerate = true)
    var Id: Int = 0
    var name: String? = ""
    var short_number: String? = ""
    var quality: String? = ""
    var brand_id: String? = ""
    var vehicle_type: String? = ""
    var image_url: String? = ""
    var concat: String? = ""
    var isSelected: Boolean = false
    var isRFSelected: Boolean = false
    var isLRSelected: Boolean = false
    var isRRSelected: Boolean = false


}
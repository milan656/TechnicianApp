package com.walkins.technician.DB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tyre_rr_detail")
class TyreRRDetail {

    @PrimaryKey(autoGenerate = true)
    var id = 0
    var vehicleMake: String? = ""
    var vehicleMakeId: String? = ""
    var vehiclePattern: String? = ""
    var vehiclePatternId: String? = ""
    var vehicleSize: String? = ""
    var vehicleSizeId: String? = ""
    var manufaturingDate: String? = ""
    var psiInTyreService: String? = ""
    var psiOutTyreService: String? = ""
    var weightTyreService: String? = ""

    var sidewell: String? = ""
    var shoulder: String? = ""
    var treadWear: String? = ""
    var treadDepth: String? = ""
    var rimDamage: String? = ""
    var bubble: String? = ""
    var issueResolvedArr: ArrayList<String>? = ArrayList()
    var visualDetailPhotoUrl: String? = ""

}
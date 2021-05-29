package com.walkins.aapkedoorstep.DB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.walkins.aapkedoorstep.model.login.servicelistmodel.ServiceListData

@Entity(tableName = "servicelist")
class ServiceListModelClass {

    @PrimaryKey(autoGenerate = true)
    var Id: Int = 0
    var serviceId: String = ""
    var uuid: String? = ""
    var color: String? = ""
    var color_code: String? = ""
    var status: String? = ""
    var regNumber: String? = ""
    var service_user_name: String? = ""
    var service_user_mobile: String? = ""
    var make: String? = ""
    var model: String? = ""
    var make_id: Int? = -1
    var model_id: Int? = -1
    var model_image: String? = ""
    var service: List<ServiceListData>? = ArrayList()
    var image: String? = ""
    var buildingName: String? = ""
    var address: String? = ""
    var comment_id: List<Int>? = ArrayList()
    var building_uuid:String?=""


}
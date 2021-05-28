package com.walkins.aapkedoorstep.DB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "servicelistdashboard")
class ServiceDashboardModelClass {

    @PrimaryKey(autoGenerate = true)
    var Id: Int = 0
    var buildingnamearea: String? = ""
    var address: String? = ""
    var date: String? = ""
    var buildinguuid: String? = ""
    var dateformated: String? = ""
    var openjobs: Int? = -1
    var completedjobs: Int? = -1
    var skippedjobs: Int = -1
    var totaljobs: Int = -1
    var startdate: Long = 0L
    var updatedate: Long = 0L


}
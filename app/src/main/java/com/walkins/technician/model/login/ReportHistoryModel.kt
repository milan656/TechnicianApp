package com.walkins.technician.model.login

import com.walkins.technician.model.login.servicelistmodel.ServiceListData
import com.walkins.technician.model.login.servicelistmodel.ServiceListData_2

data class ReportHistoryModel(
    var uuid:String,
    var fullAddress:String,
    var regNumber: Int,
    var makeModel: String,
    var carColor: String,
    var color_code: String,
    var dateFormated: String,
    var comment_id: Int,
    var carImageURL: String,
    var completedCount: Int,
    var skippedCount: Int,
    var serviceList:ArrayList<ServiceListData>,
    var carCount: Int,
    var createdAt: Long = 0,
    val updatedAt: Long = 0

)
package com.walkins.aapkedoorstep.model.login

import com.walkins.aapkedoorstep.model.login.servicelistmodel.ServiceListData
import com.walkins.aapkedoorstep.model.login.servicelistmodel.ServiceListData_2

data class ReportHistoryModel(
    var uuid:String,
    var fullAddress:String,
    var regNumber: String,
    var makeModel: String,
    var carColor: String,
    var color_code: String,
    var dateFormated: String,
    var comment_id: Int,
    var carImageURL: String,
    var serviceList:ArrayList<ServiceListData>,
    var createdAt: Long = 0,
    val updatedAt: Long = 0

)
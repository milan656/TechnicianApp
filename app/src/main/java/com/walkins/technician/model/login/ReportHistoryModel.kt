package com.walkins.technician.model.login

import com.walkins.technician.model.login.servicelistmodel.ServiceListData

data class ReportHistoryModel(
    var color: String,
    var makeModel: String,
    var status: String,
    var createdAt: Long = 0,
    val updatedAt: Long = 0,
    var service: ArrayList<ServiceListData>,
    var regNumber: Int,
    var actualServiceDate: String

)
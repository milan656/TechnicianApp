package com.walkins.aapkedoorstep.model.login

data class NotificationModel(
    var addressTitle: String,
    var fullAddress: String,
    var date: String,
    var building_uuid:String,
    var dateFormated: String,
    var upcomingCount: Int,
    var completedCount: Int,
    var skippedCount: Int,
    var carCount: Int,
    var createdAt: Long = 0,
    val updatedAt: Long = 0

)
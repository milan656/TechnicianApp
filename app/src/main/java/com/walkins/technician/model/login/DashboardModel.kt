package com.walkins.technician.model.login

data class DashboardModel(
    var addressTitle: String,
    var upcomingCount: Int,
    var completedCount: Int,
    var skippedCount: Int,
    var carCount: Int,
    var createdAt: Long = 0,
    val updatedAt: Long = 0

)
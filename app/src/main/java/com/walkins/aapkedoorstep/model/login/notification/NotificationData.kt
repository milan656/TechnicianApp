package com.walkins.aapkedoorstep.model.login.notification

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NotificationData(
    @SerializedName("notifications")
    @Expose
    val notifications: List<Notification>,
    @SerializedName("total_count")
    @Expose
    val totalCount: Int
)
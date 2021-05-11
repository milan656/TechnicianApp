package com.walkins.aapkedoorstep.model.login.notification

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("uuid")
    @Expose
    val uuid: String,
    @SerializedName("title")
    @Expose
    val title: String,
    @SerializedName("message")
    @Expose
    val message: String,
    @SerializedName("notification_type")
    @Expose
    val notificationType: String,
    @SerializedName("type")
    @Expose
    val type: String,
    @SerializedName("action")
    @Expose
    val action: String,
    @SerializedName("image_url")
    @Expose
    val imageUrl: String,
    @SerializedName("read")
    @Expose
    val read: Boolean,
    @SerializedName("notification_data")
    @Expose
    val notificationData: String,
    @SerializedName("read_at")
    @Expose
    val readAt: String,
    @SerializedName("created_at")
    @Expose
    val createdAt: String,
    @SerializedName("updated_at")
    @Expose
    val updatedAt: String
)
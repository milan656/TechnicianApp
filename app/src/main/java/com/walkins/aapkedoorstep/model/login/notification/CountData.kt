package com.walkins.aapkedoorstep.model.login.notification

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CountData(
    @SerializedName("count")
    @Expose
    var count: Int
)
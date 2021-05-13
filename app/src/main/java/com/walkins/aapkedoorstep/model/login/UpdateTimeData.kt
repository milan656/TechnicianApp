package com.walkins.aapkedoorstep.model.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UpdateTimeData(
    @SerializedName("last_tyre_brand_updated_date")
    @Expose
    val lastTyreBrandUpdatedDate: String,
    @SerializedName("last_size_updated_date")
    @Expose
    val lastSizeUpdatedDate: String,
    @SerializedName("last_pattern_updated_date")
    @Expose
    val lastPatternUpdatedDate: String
)
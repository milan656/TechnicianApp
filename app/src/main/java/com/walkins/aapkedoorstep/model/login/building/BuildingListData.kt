package com.walkins.aapkedoorstep.model.login.building

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BuildingListData(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("uuid")
    @Expose
    var uuid: String,
    @SerializedName("address")
    @Expose
    var address: String

)
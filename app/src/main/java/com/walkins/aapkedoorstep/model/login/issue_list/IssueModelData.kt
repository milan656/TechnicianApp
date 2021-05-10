package com.walkins.aapkedoorstep.model.login.issue_list

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class IssueModelData(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("name")
    @Expose
    var name: String
)
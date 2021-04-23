package com.walkins.technician.model.login.issue_list

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class IssueListModel(
    @SerializedName("success")
    @Expose
    var success: Boolean,
    @SerializedName("message")
    @Expose
    var message: String
)
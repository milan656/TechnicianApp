package com.walkins.technician.model.login.comment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CommentListData(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("name")
    @Expose
    var name: String
)
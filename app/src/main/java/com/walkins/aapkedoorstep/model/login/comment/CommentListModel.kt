package com.walkins.aapkedoorstep.model.login.comment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jkadvantage.model.error.ErrorModel

data class CommentListModel(
    @SerializedName("success")
    @Expose
    var success: Boolean,
    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("data")
    @Expose
    var data: List<CommentListData>,
    @SerializedName("error")
    @Expose
    val error: ArrayList<ErrorModel>
)
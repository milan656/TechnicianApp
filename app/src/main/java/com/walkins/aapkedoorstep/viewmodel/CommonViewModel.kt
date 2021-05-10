package com.walkins.aapkedoorstep.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.google.gson.JsonObject
import com.jkadvantage.model.notification.saveToken.SaveTokenModel
import com.walkins.aapkedoorstep.model.login.UserInfoModel
import com.walkins.aapkedoorstep.model.login.comment.CommentListModel
import com.walkins.aapkedoorstep.model.login.issue_list.IssueListModel
import com.walkins.aapkedoorstep.model.login.service.ServiceModel
import com.walkins.aapkedoorstep.model.login.servicemodel.servicedata.ServiceDataByIdModel
import com.walkins.aapkedoorstep.repository.CommonRepo
import com.walkins.aapkedoorstep.repository.ServiceRepo

class CommonViewModel : ViewModel() {

    private var commonRepo: CommonRepo? = null
    private var issueListModel: MutableLiveData<IssueListModel>? = null
    private var serviceModel: MutableLiveData<ServiceModel>? = null
    private var commentListModel: MutableLiveData<CommentListModel>? = null
    private var serviceByIdModel: MutableLiveData<ServiceDataByIdModel>? = null
    private var userInfo: MutableLiveData<UserInfoModel>? = null
    private var saveToken: MutableLiveData<SaveTokenModel>? = null

    fun callApiListOfIssue(
        accessToken: String,
        context: Context
    ) {
        commonRepo = CommonRepo().getInstance()
        issueListModel = commonRepo?.getListOfIssue(context, accessToken)
    }

    fun getListOfIssue(): LiveData<IssueListModel> {
        return issueListModel!!
    }

    fun callApiGetService(
        accessToken: String,
        context: Context
    ) {
        commonRepo = CommonRepo().getInstance()
        serviceModel = commonRepo?.getService(context, accessToken)
    }

    fun getService(): LiveData<ServiceModel> {
        return serviceModel!!
    }

    fun callApiGetComments(
        accessToken: String,
        context: Context
    ) {
        commonRepo = CommonRepo().getInstance()
        commentListModel = commonRepo?.getCommentList(context, accessToken)
    }

    fun getCommentList(): LiveData<CommentListModel> {
        return commentListModel!!
    }

    fun callApiGetServiceById(
        jsonObject: JsonObject,
        accessToken: String,
        context: Context
    ) {
        commonRepo = CommonRepo().getInstance()
        serviceByIdModel = commonRepo?.getServiceById(jsonObject, context, accessToken)
    }

    fun getServiceById(): LiveData<ServiceDataByIdModel> {
        return serviceByIdModel!!
    }

    fun callApiGetUserInfo(
        accessToken: String,
        context: Context
    ) {
        commonRepo = CommonRepo().getInstance()
        userInfo = commonRepo?.getUserInfo(context, accessToken)
    }

    fun getUserInfo(): LiveData<UserInfoModel> {
        return userInfo!!
    }

    fun callApiLogoutFromAll(accessToken: String, context: Context) {
        commonRepo = CommonRepo().getInstance()
        userInfo = commonRepo?.callApiLogoutFromAll(context, accessToken)
    }

    fun callApiToSaveToken(jsonObject: JsonObject,accessToken: String, context: Context) {
        commonRepo = CommonRepo().getInstance()
        saveToken = commonRepo?.saveTokenToDatabase(jsonObject,accessToken, context)
    }

    fun getSaveToken(): LiveData<SaveTokenModel>? {
        return saveToken!!
    }
}
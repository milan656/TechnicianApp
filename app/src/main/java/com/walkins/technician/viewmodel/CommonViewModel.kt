package com.walkins.technician.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.walkins.technician.model.login.issue_list.IssueListModel
import com.walkins.technician.model.login.service.ServiceModel
import com.walkins.technician.repository.CommonRepo
import com.walkins.technician.repository.ServiceRepo

class CommonViewModel : ViewModel() {

    private var commonRepo: CommonRepo? = null
    private var issueListModel: MutableLiveData<IssueListModel>? = null
    private var serviceModel: MutableLiveData<ServiceModel>? = null

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

}
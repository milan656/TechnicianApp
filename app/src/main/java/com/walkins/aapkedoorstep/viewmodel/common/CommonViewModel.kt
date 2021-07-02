package com.walkins.aapkedoorstep.viewmodel.common

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.technician.common.Common
import com.facebook.AccessToken
import com.google.gson.JsonObject
import com.jkadvantagandbadsha.model.login.UserModel
import com.jkadvantage.model.notification.saveToken.SaveTokenModel
import com.walkins.aapkedoorstep.model.login.UserInfoModel
import com.walkins.aapkedoorstep.model.login.comment.CommentListModel
import com.walkins.aapkedoorstep.model.login.issue_list.IssueListModel
import com.walkins.aapkedoorstep.model.login.notification.NotificationCountModel
import com.walkins.aapkedoorstep.model.login.notification.NotificationModel
import com.walkins.aapkedoorstep.model.login.service.ServiceModel
import com.walkins.aapkedoorstep.model.login.servicemodel.servicedata.ServiceDataByIdModel
import com.walkins.aapkedoorstep.repository.CommonRepo
import com.walkins.aapkedoorstep.repository.ServiceRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CommonViewModel(private var commonRepo: CommonRepo) : ViewModel() {

    var issueListModel: MutableLiveData<IssueListModel>? = MutableLiveData()
    var serviceModel: MutableLiveData<ServiceModel>? = MutableLiveData()
    var commentListModel: MutableLiveData<CommentListModel>? = MutableLiveData()
    var notificationModel: MutableLiveData<NotificationModel>? = MutableLiveData()
    var notificationCountModel: MutableLiveData<NotificationCountModel>? = MutableLiveData()
    var serviceByIdModel: MutableLiveData<ServiceDataByIdModel>? = MutableLiveData()
    var userInfo: MutableLiveData<UserInfoModel>? = MutableLiveData()
    var saveToken: MutableLiveData<SaveTokenModel>? = MutableLiveData()

    fun callApiListOfIssue(
        accessToken: String,
        context: Context,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = commonRepo.getListOfIssue(context,
                accessToken)
            withContext(Dispatchers.Main) {
                if (res.isSuccessful) {
                    try {
                        issueListModel?.value = Common.getModelReturn_("IssueListModel", res, 0, context) as IssueListModel?
                    }catch (e:Exception){
                        e.printStackTrace()
                    }

                } else {

                    try {
                        issueListModel?.value = Common.getModelReturn_("IssueListModel", res, 1, context) as IssueListModel?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun callApiGetService(
        accessToken: String,
        context: Context,
    ) {

        viewModelScope.launch(Dispatchers.IO) {
            val res = commonRepo.getService(context, accessToken)
            withContext(Dispatchers.Main) {
                if (res.isSuccessful) {
                    try {
                        serviceModel?.value = Common.getModelReturn_("ServiceModel", res, 0, context) as ServiceModel?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    try {
                        serviceModel?.value = Common.getModelReturn_("ServiceModel", res, 1, context) as ServiceModel?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun callApiGetComments(
        accessToken: String,
        context: Context,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = commonRepo.getCommentList(context, accessToken)
            withContext(Dispatchers.Main) {
                if (res.isSuccessful) {
                    try {
                        commentListModel?.value = Common.getModelReturn_("CommentListModel", res, 0, context) as CommentListModel?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    try {
                        commentListModel?.value = Common.getModelReturn_("CommentListModel", res, 1, context) as CommentListModel?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun callApiGetNotificationList(
        accessToken: String,
        context: Context
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = commonRepo.getNotificationList(context, accessToken)
            withContext(Dispatchers.Main) {
                if (res.isSuccessful) {
                    try {
                        notificationModel?.value = Common.getModelReturn_("NotificationModel", res, 0, context) as NotificationModel?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    try {
                        notificationModel?.value = Common.getModelReturn_("NotificationModel", res, 1, context) as NotificationModel?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun callApiGetNotificationCount(
        accessToken: String,
        context: Context,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = commonRepo.getNotificationCount(context, accessToken)
            withContext(Dispatchers.Main) {
                if (res.isSuccessful) {
                    try {
                        notificationCountModel?.value = Common.getModelReturn_("NotificationCountModel", res, 0, context) as NotificationCountModel?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    try {
                        notificationCountModel?.value = Common.getModelReturn_("NotificationCountModel", res, 1, context) as NotificationCountModel?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun callApiGetServiceById(
        jsonObject: JsonObject,
        accessToken: String,
        context: Context,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = commonRepo.getServiceById(jsonObject, context, accessToken)
            withContext(Dispatchers.Main) {
                if (res.isSuccessful) {
                    try {
                        serviceByIdModel?.value = Common.getModelReturn_("ServiceDataByIdModel", res, 0, context) as ServiceDataByIdModel?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    try {
                        serviceByIdModel?.value = Common.getModelReturn_("ServiceDataByIdModel", res, 1, context) as ServiceDataByIdModel?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun callApiGetUserInfo(
        accessToken: String,
        context: Context,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = commonRepo.getUserInfo(context, accessToken)
            withContext(Dispatchers.Main) {
                if (res.isSuccessful) {
                    try {
                        userInfo?.value = Common.getModelReturn_("UserInfoModel", res, 0, context) as UserInfoModel?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    try {
                        userInfo?.value = Common.getModelReturn_("UserInfoModel", res, 1, context) as UserInfoModel?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun callApiLogoutFromAll(accessToken: String, context: Context) {
        commonRepo = CommonRepo().getInstance()
        userInfo = commonRepo.callApiLogoutFromAll(context, accessToken)

    }

    fun callApiToSaveToken(jsonObject: JsonObject, accessToken: String, context: Context) {

        viewModelScope.launch(Dispatchers.IO) {
            val res =commonRepo.saveTokenToDatabase(jsonObject, accessToken, context)
            withContext(Dispatchers.Main) {
                if (res.isSuccessful) {
                    try {
                        saveToken?.value = Common.getModelReturn_("SaveTokenModel", res, 0, context) as SaveTokenModel?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    try {
                        saveToken?.value = Common.getModelReturn_("SaveTokenModel", res, 1, context) as SaveTokenModel?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }


}
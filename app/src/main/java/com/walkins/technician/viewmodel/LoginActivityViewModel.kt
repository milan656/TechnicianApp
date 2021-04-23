package com.walkins.technician.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.walkins.technician.repository.LoginRepository
import com.jkadvantagandbadsha.model.login.UserModel
import com.jkadvantage.model.customerIntraction.uploadimage.UploadImageModel
import okhttp3.MultipartBody


class LoginActivityViewModel : ViewModel() {

    private var loginRepository: LoginRepository? = null
    var userModelData: MutableLiveData<UserModel>? = null
    var uploadImageModel: MutableLiveData<UploadImageModel>? = null


    fun getLoginData(): LiveData<UserModel>? {
        return userModelData
    }

    fun init(
        userId: String,
        password: String,
        grantType: String,
        authorizationToke: String,
        versionCode: Int,
        deviceName: String?,
        androidOS: String,
        deviceType: String?
    ) {

        loginRepository = LoginRepository().getInstance()
        userModelData = loginRepository!!.loginUser(
            userId,
            password,
            grantType,
            authorizationToke,
            versionCode,
            deviceName,
            androidOS
        )

    }

    fun initTwo(
        url: String,
        userId: String,
        password: String,
        grantType: String,
        authorizationToke: String,
        versionCode: Int,
        deviceName: String?,
        androidOS: String,
        deviceType: String?
    ) {

        loginRepository = LoginRepository().getInstance()
        userModelData = loginRepository!!.loginUserTwo(
            url,
            userId,
            password,
            grantType,
            authorizationToke,
            versionCode,
            deviceName,
            androidOS
        )

    }

    fun refreshToken(
        authorizationToke: String?,
        grantType: String?,
        refreshToken: String?
    ) {

        loginRepository = LoginRepository().getInstance()
        userModelData = loginRepository!!.refreshToken(
            authorizationToke!!,
            grantType!!, refreshToken
        )

    }

    fun uploadImage(
        multiPart: MultipartBody.Part,
        authorizationToke: String,
        context: Context,
        type: String
    ) {
        loginRepository = LoginRepository().getInstance()
        uploadImageModel =
            loginRepository?.uploadImage(multiPart, type, authorizationToke, context)
    }

    fun getImageUpload(): LiveData<UploadImageModel>? {
        return uploadImageModel!!
    }


}
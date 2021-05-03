package com.walkins.technician.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.walkins.technician.repository.LoginRepository
import com.jkadvantagandbadsha.model.login.UserModel
import com.jkadvantage.model.customerIntraction.uploadimage.UploadImageModel
import com.walkins.technician.model.login.otp.OtpModel
import okhttp3.MultipartBody


class LoginActivityViewModel : ViewModel() {

    private var loginRepository: LoginRepository? = null
    var userModelData: MutableLiveData<UserModel>? = null
    var uploadImageModel: MutableLiveData<UploadImageModel>? = null
    var otpModel: MutableLiveData<OtpModel>? = null


    fun getLoginData(): LiveData<UserModel>? {
        return userModelData
    }

    fun callApiSendOtp(jsonObject: JsonObject, context: Context) {

        loginRepository = loginRepository?.getInstance()
        otpModel = loginRepository?.callApiSendOtp(jsonObject, context)

    }

    fun sendOtp(): LiveData<OtpModel>? {
        return otpModel
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

    fun initCallSendOtp(
        jsonObject: JsonObject, context: Context
    ) {

        loginRepository = LoginRepository().getInstance()
        otpModel = loginRepository!!.callApiSendOtp(
            jsonObject, context
        )

    }

    fun initTwo(
        jsonObject: JsonObject,

    ) {

        loginRepository = LoginRepository().getInstance()
        otpModel = loginRepository!!.loginUserTwo(
            jsonObject
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
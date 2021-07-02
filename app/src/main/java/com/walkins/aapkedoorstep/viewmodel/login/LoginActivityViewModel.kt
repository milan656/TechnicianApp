package com.walkins.aapkedoorstep.viewmodel.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.technician.common.Common
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.walkins.aapkedoorstep.repository.LoginRepository
import com.jkadvantagandbadsha.model.login.UserModel
import com.jkadvantage.model.customerIntraction.uploadimage.UploadImageModel
import com.walkins.aapkedoorstep.model.login.otp.OtpModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody


class LoginActivityViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    var userModelData: MutableLiveData<UserModel>? = MutableLiveData()
    var uploadImageModel: MutableLiveData<UploadImageModel>? = null
    var otpModel: MutableLiveData<OtpModel>? = MutableLiveData()

    fun init(
        context: Context,
        userId: String,
        password: String,
        grantType: String,
        authorizationToke: String,
        versionCode: Int,
        deviceName: String?,
        androidOS: String,
        deviceType: String?,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = loginRepository.loginUser(userId,
                password,
                grantType,
                authorizationToke,
                versionCode,
                deviceName,
                androidOS)
            withContext(Dispatchers.Main) {
                if (res.isSuccessful) {
                    try {
                        userModelData?.value = Common.getModelReturn_("UserModel", res, 0, context) as UserModel?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    try {
                        userModelData?.value = Common.getModelReturn_("UserModel", res, 1, context) as UserModel?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun initTwo(
        context: Context,
        jsonObject: JsonObject,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = loginRepository.loginUserTwo(jsonObject)
            withContext(Dispatchers.Main) {
                if (res.isSuccessful) {
                    try {
                        otpModel?.value = Common.getModelReturn_("OtpModel", res, 0, context) as OtpModel?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    try {
                        otpModel?.value = Common.getModelReturn_("OtpModel", res, 1, context) as OtpModel?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

    }

    fun refreshToken(
        authorizationToke: String?,
        grantType: String?,
        refreshToken: String?,
    ) {

        userModelData = loginRepository.refreshToken(
            authorizationToke!!,
            grantType!!, refreshToken
        )

    }

    fun uploadImage(
        multiPart: MultipartBody.Part,
        authorizationToke: String,
        context: Context,
        type: String,
    ) {
        uploadImageModel =
            loginRepository.uploadImage(multiPart, type, authorizationToke, context)
    }

    fun getImageUpload(): LiveData<UploadImageModel>? {
        return uploadImageModel!!
    }


}
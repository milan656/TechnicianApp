package com.walkins.technician.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.technician.common.Common
import com.example.technician.common.RetrofitCommonClass
import com.google.gson.JsonObject
import com.walkins.technician.networkApi.login.LoginApi
import com.jkadvantagandbadsha.model.login.UserModel
import com.jkadvantage.model.customerIntraction.uploadimage.UploadImageModel
import com.walkins.technician.model.login.otp.OtpModel
import com.walkins.technician.model.login.servicemodel.AddServiceModel
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class LoginRepository {

    var loginDataRespotitory: LoginRepository? = null

    var loginApi: LoginApi

    constructor() {
        loginApi = RetrofitCommonClass.createService(LoginApi::class.java)
    }


    companion object

    fun getInstance(): LoginRepository {
        if (loginDataRespotitory == null) {
            loginDataRespotitory = LoginRepository()
        }
        return loginDataRespotitory as LoginRepository
    }

    fun callApiSendOTP(
        authorizationToke: String,
        jsonObject: JsonObject,
        context: Context
    ): MutableLiveData<OtpModel> {
        val loginData = MutableLiveData<OtpModel>()
        loginApi.callApiSendOTP(jsonObject
        ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    loginData.value = Common.getModelreturn(
                        "OtpModel",
                        response,
                        0,
                        context
                    ) as OtpModel?
                } else {
                    try {
                        loginData.value = Common.getModelreturn(
                            "OtpModel",
                            response,
                            1,
                            context
                        ) as OtpModel?
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                t.printStackTrace()
            }
        })
        return loginData
    }

    fun loginUser(
        userId: String,
        password: String,
        grantType: String,
        authorizationToke: String,
        versionCode: Int,
        deviceName: String?,
        androidOS: String
    ): MutableLiveData<UserModel> {
        val loginData = MutableLiveData<UserModel>()
        loginApi.loginUser(
            userId,
            password,
            grantType,
            authorizationToke
        ).enqueue(object : Callback<UserModel> {
            override fun onResponse(
                call: Call<UserModel>,
                response: Response<UserModel>
            ) {
                if (response.isSuccessful) {
                    loginData.value = response?.body()
                } else {
                    try {
                        val responce = response.errorBody()?.string()
                        val jsonObjectError = JSONObject(responce)

                        val userModel: UserModel =
                            Common.getErrorModel(jsonObjectError, "UserModel") as UserModel
                        //recoveryData.setValue(recoveryPasswordModel)


                        loginData.value = userModel

                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {

                t?.printStackTrace()
            }
        })
        return loginData
    }

    fun loginUserTwo(
        url: String,
        userId: String,
        password: String,
        grantType: String,
        authorizationToke: String,
        versionCode: Int,
        deviceName: String?,
        androidOS: String
    ): MutableLiveData<UserModel> {
        val loginData = MutableLiveData<UserModel>()
        loginApi.loginUserTwo(
            url,
            userId,
            password,
            grantType,
            authorizationToke,
            versionCode,
            deviceName,
            androidOS
        ).enqueue(object : Callback<UserModel> {
            override fun onResponse(
                call: Call<UserModel>,
                response: Response<UserModel>
            ) {
                if (response.isSuccessful) {
                    loginData.value = response?.body()
                } else {
                    try {
                        val responce = response.errorBody()?.string()
                        val jsonObjectError = JSONObject(responce)

                        val userModel: UserModel =
                            Common.getErrorModel(jsonObjectError, "UserModel") as UserModel
                        loginData.value = userModel
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {

            }
        })
        return loginData
    }

    fun refreshToken(
        authorizationToke: String,
        grantType: String,
        refreshToken: String?
    ): MutableLiveData<UserModel> {
        val loginData = MutableLiveData<UserModel>()
        loginApi.refreshToken(
            authorizationToke,
            grantType,
            refreshToken!!
        ).enqueue(object : Callback<UserModel> {
            override fun onResponse(
                call: Call<UserModel>,
                response: Response<UserModel>
            ) {
                if (response.isSuccessful) {
                    loginData.value = response?.body()
                } else {
                    try {
                        val responce = response.errorBody()?.string()
                        val jsonObjectError = JSONObject(responce!!)

                        val userModel: UserModel =
                            Common.getErrorModel(jsonObjectError, "UserModel") as UserModel

                        loginData.value = userModel

                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {

            }
        })
        return loginData
    }

    fun uploadImage(
        jsonObject: MultipartBody.Part,
        type: String,
        authorizationToke: String, context: Context
    ): MutableLiveData<UploadImageModel> {
        val loginData = MutableLiveData<UploadImageModel>()
        loginApi.uploadFile(jsonObject, authorizationToke, type)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        loginData.value = Common?.getModelreturn(
                            "UploadImageModel",
                            response,
                            0,
                            context
                        ) as UploadImageModel?

                    } else {
                        try {
                            loginData.value = Common?.getModelreturn(
                                "UploadImageModel",
                                response,
                                1,
                                context
                            ) as UploadImageModel?

                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                }
            })
        return loginData
    }
}
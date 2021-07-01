package com.walkins.aapkedoorstep.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.technician.common.Common
import com.example.technician.common.RetrofitCommonClass
import com.google.gson.JsonObject
import com.walkins.aapkedoorstep.networkApi.login.LoginApi
import com.jkadvantagandbadsha.model.login.UserModel
import com.jkadvantage.model.customerIntraction.uploadimage.UploadImageModel
import com.walkins.aapkedoorstep.model.login.makemodel.VehicleModel
import com.walkins.aapkedoorstep.model.login.otp.OtpModel
import com.walkins.aapkedoorstep.model.login.servicemodel.AddServiceModel
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

    fun callApiSendOtp(
        jsonObject: JsonObject,
        context: Context
    ): MutableLiveData<OtpModel> {
        var servicedata = MutableLiveData<OtpModel>()

        var addEdit: Call<ResponseBody>? = loginApi.callApiSendOTP(jsonObject)

        addEdit?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>
            ) = if (response.isSuccessful) {
                servicedata.value = Common?.getModelreturn(
                    "OtpModel",
                    response,
                    0,
                    context
                ) as OtpModel?

            } else {
                try {
                    servicedata.value = Common?.getModelreturn(
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

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("getCipResponse", "" + t.cause + " " + t.message)
//                servicedata.value = null
            }
        })
        return servicedata
    }

    fun loginUserTwo(
        jsonObject: JsonObject/*,

        userId: String,
        password: String,
        grantType: String,
        authorizationToke: String,
        versionCode: Int,
        deviceName: String?,
        androidOS: String*/
    ): MutableLiveData<OtpModel> {
        val loginData = MutableLiveData<OtpModel>()
        loginApi.loginUserTwo(
            jsonObject,

            /* userId,
             password,
             grantType,
             authorizationToke,
             versionCode,
             deviceName,
             androidOS*/
        ).enqueue(object : Callback<OtpModel> {
            override fun onResponse(
                call: Call<OtpModel>,
                response: Response<OtpModel>
            ) {
                if (response.isSuccessful) {
                    loginData.value = response.body()
                } else {
                    try {
                        val responce = response.errorBody()?.string()
                        val jsonObjectError = JSONObject(responce)

                        val userModel: OtpModel =
                            Common.getErrorModel(jsonObjectError, "OtpModel") as OtpModel
                        loginData.value = userModel
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<OtpModel>, t: Throwable) {

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
//        contentType: String,
        authorizationToke: String, context: Context
    ): MutableLiveData<UploadImageModel> {
        val loginData = MutableLiveData<UploadImageModel>()
        loginApi.uploadFile(jsonObject, authorizationToke,  type)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        loginData.value = Common.getModelreturn(
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
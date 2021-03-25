package com.example.technicianapp.repository

import androidx.lifecycle.MutableLiveData
import com.example.technician.common.Common
import com.example.technician.common.RetrofitCommonClass
import com.example.technicianapp.networkApi.login.LoginApi
import com.jkadvantagandbadsha.model.login.UserModel
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


}
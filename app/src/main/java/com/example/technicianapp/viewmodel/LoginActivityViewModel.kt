package com.example.technicianapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.technicianapp.repository.LoginRepository
import com.jkadvantagandbadsha.model.login.UserModel


class LoginActivityViewModel : ViewModel(){

    private var loginRepository: LoginRepository? = null
    var userModelData : MutableLiveData<UserModel>? = null


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
        userModelData = loginRepository!!.loginUser(userId , password , grantType , authorizationToke , versionCode , deviceName , androidOS)

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
        userModelData = loginRepository!!.loginUserTwo(url ,userId , password , grantType , authorizationToke , versionCode , deviceName , androidOS)

    }

    fun refreshToken(
        authorizationToke: String?,
        grantType: String?,
        refreshToken: String?
    ) {

        loginRepository = LoginRepository().getInstance()
        userModelData = loginRepository!!.refreshToken(authorizationToke!!,
            grantType!!, refreshToken)

    }


}
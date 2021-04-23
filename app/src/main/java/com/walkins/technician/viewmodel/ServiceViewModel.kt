package com.walkins.technician.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.walkins.technician.model.login.servicemodel.AddServiceModel
import com.walkins.technician.repository.LoginRepository
import com.walkins.technician.repository.ServiceRepo

class ServiceViewModel : ViewModel() {

    private var serviceRepo: ServiceRepo? = null
    private var addServiceModel: MutableLiveData<AddServiceModel>? = null

    fun callApiAddService(
        jsonObject: JsonObject,
        access_token: String,
        context: Context
    ) {
        serviceRepo = ServiceRepo().getInstance()
        addServiceModel = serviceRepo?.addService(
            jsonObject,
            access_token,
            context
        )
    }

    fun getAddService(): LiveData<AddServiceModel>? {
        return addServiceModel
    }

}
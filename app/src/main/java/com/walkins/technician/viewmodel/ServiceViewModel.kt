package com.walkins.technician.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.walkins.technician.model.login.dashboard_model.DashboardServiceListModel
import com.walkins.technician.model.login.servicelistmodel.ServiceListByDateModel
import com.walkins.technician.model.login.servicemodel.AddServiceModel
import com.walkins.technician.repository.LoginRepository
import com.walkins.technician.repository.ServiceRepo

class ServiceViewModel : ViewModel() {

    private var serviceRepo: ServiceRepo? = null
    private var addServiceModel: MutableLiveData<AddServiceModel>? = null
    private var dashboardServiceListModel: MutableLiveData<DashboardServiceListModel>? = null
    private var serviceListByDateModel: MutableLiveData<ServiceListByDateModel>? = null

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

    fun callApiDashboardService(
        date: String,
        access_token: String,
        context: Context
    ) {
        serviceRepo = ServiceRepo().getInstance()
        dashboardServiceListModel = serviceRepo?.getDashboardService(
            date,
            access_token,
            context
        )
    }

    fun getDashboardService(): LiveData<DashboardServiceListModel>? {
        return dashboardServiceListModel
    }

    fun callApiServiceByDate(
        date: String,
        access_token: String,
        context: Context
    ) {
        serviceRepo = ServiceRepo().getInstance()
        serviceListByDateModel = serviceRepo?.getServiceByDate(
            date,
            access_token,
            context
        )
    }

    fun getServiceByDate(): LiveData<ServiceListByDateModel>? {
        return serviceListByDateModel
    }

}
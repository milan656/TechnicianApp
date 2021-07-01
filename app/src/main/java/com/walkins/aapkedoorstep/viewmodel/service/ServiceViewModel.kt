package com.walkins.aapkedoorstep.viewmodel.service

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.technician.common.Common
import com.google.gson.JsonObject
import com.walkins.aapkedoorstep.model.login.ReportHistoryModel
import com.walkins.aapkedoorstep.model.login.ReportServiceModel
import com.walkins.aapkedoorstep.model.login.dashboard_model.DashboardServiceListModel
import com.walkins.aapkedoorstep.model.login.notification.NotificationModel
import com.walkins.aapkedoorstep.model.login.servicelistmodel.ServiceListByDateModel
import com.walkins.aapkedoorstep.model.login.servicemodel.AddServiceModel
import com.walkins.aapkedoorstep.repository.LoginRepository
import com.walkins.aapkedoorstep.repository.ServiceRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ServiceViewModel(private val serviceRepo: ServiceRepo) : ViewModel() {

    private var addServiceModel: MutableLiveData<AddServiceModel>? = MutableLiveData()
    private var dashboardServiceListModel: MutableLiveData<DashboardServiceListModel>? = null
    private var serviceListByDateModel: MutableLiveData<ServiceListByDateModel>? = null
    private var reportHistoryModel: MutableLiveData<ReportServiceModel>? = null

    fun callApiAddService(
        jsonObject: JsonObject,
        access_token: String,
        context: Context,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = serviceRepo.addService(
                jsonObject,
                access_token,
                context
            )
            withContext(Dispatchers.Main) {
                if (res.isSuccessful) {
                    addServiceModel?.value = Common.getModelReturn_("AddServiceModel", res, 0, context) as AddServiceModel?
                } else {
                    addServiceModel?.value = Common.getModelReturn_("AddServiceModel", res, 1, context) as AddServiceModel?
                }
            }
        }
    }

    fun callApiUpdateService(
        jsonObject: JsonObject,
        access_token: String,
        context: Context,
    ) {
        addServiceModel = serviceRepo?.UpdateService(
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
        context: Context,
    ) {
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
        building_id: String,
        access_token: String,
        context: Context,
    ) {
        serviceListByDateModel = serviceRepo?.getServiceByDate(
            date,
            building_id,
            access_token,
            context
        )
    }

    fun getServiceByDate(): LiveData<ServiceListByDateModel>? {
        return serviceListByDateModel
    }

    fun callApiReportList(
        jsonObject: JsonObject,
        access_token: String,
        context: Context,
    ) {
        reportHistoryModel = serviceRepo?.callApiReportService(jsonObject,
            access_token,
            context
        )
    }

    fun getReportservice(): LiveData<ReportServiceModel>? {
        return reportHistoryModel
    }

}
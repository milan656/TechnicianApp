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

    var addServiceModel: MutableLiveData<AddServiceModel>? = MutableLiveData()
    var dashboardServiceListModel: MutableLiveData<DashboardServiceListModel>? = MutableLiveData()
    var serviceListByDateModel: MutableLiveData<ServiceListByDateModel>? = MutableLiveData()
    var reportHistoryModel: MutableLiveData<ReportServiceModel>? = MutableLiveData()

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
                    try {
                        addServiceModel?.value = Common.getModelReturn_("AddServiceModel", res, 0, context) as AddServiceModel?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    try {
                        addServiceModel?.value = Common.getModelReturn_("AddServiceModel", res, 1, context) as AddServiceModel?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun callApiUpdateService(
        jsonObject: JsonObject,
        access_token: String,
        context: Context,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = serviceRepo.UpdateService(
                jsonObject,
                access_token,
                context
            )
            withContext(Dispatchers.Main) {
                if (res.isSuccessful) {
                    try {
                        addServiceModel?.value = Common.getModelReturn_("AddServiceModel", res, 0, context) as AddServiceModel?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    try {
                        addServiceModel?.value = Common.getModelReturn_("AddServiceModel", res, 1, context) as AddServiceModel?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }


    fun callApiDashboardService(
        date: String,
        access_token: String,
        context: Context,
    ) {
        dashboardServiceListModel = serviceRepo.getDashboardService(
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
        serviceRepo.getServiceByDate(
            date,
            building_id,
            access_token,
            context
        )


    }

    fun callApiServiceByDate_(
        date: String,
        building_id: String,
        access_token: String,
        context: Context,
    ) {


        viewModelScope.launch(Dispatchers.IO) {
            val res = serviceRepo.getServiceByDate_(
                date,
                building_id,
                access_token,
                context
            )
            withContext(Dispatchers.Main) {
                if (res.isSuccessful) {
                    try {
                        serviceListByDateModel?.value = Common.getModelReturn_("ServiceListByDateModel", res, 0, context) as ServiceListByDateModel?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    try {
                        serviceListByDateModel?.value = Common.getModelReturn_("ServiceListByDateModel", res, 1, context) as ServiceListByDateModel?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun callApiReportList(
        jsonObject: JsonObject,
        access_token: String,
        context: Context,
    ) {
        /*reportHistoryModel = serviceRepo.callApiReportService(jsonObject,
            access_token,
            context
        )*/

        viewModelScope.launch(Dispatchers.IO) {
            val res = serviceRepo.callApiReportService(jsonObject,
                access_token,
                context
            )
            withContext(Dispatchers.Main) {
                if (res.isSuccessful) {
                    try {
                        reportHistoryModel?.value = Common.getModelReturn_("ReportServiceModel", res, 0, context) as ReportServiceModel?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    try {
                        reportHistoryModel?.value = Common.getModelReturn_("ReportServiceModel", res, 1, context) as ReportServiceModel?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun getReportservice(): LiveData<ReportServiceModel>? {
        return reportHistoryModel
    }

}
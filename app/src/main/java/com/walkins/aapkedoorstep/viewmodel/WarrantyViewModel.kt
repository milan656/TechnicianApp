package com.walkins.aapkedoorstep.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jkadvantage.model.vehicleBrandModel.VehicleBrandModel
import com.walkins.aapkedoorstep.model.login.patternmodel.PatternModel
import com.walkins.aapkedoorstep.model.login.sizemodel.SizeModel
import com.walkins.aapkedoorstep.repository.WarrantyRepository

class WarrantyViewModel : ViewModel() {

    private var warrantyRepository: WarrantyRepository? = null
    var vehicleTypeBrandModel: MutableLiveData<VehicleBrandModel>? = null
    var PatternModel: MutableLiveData<PatternModel>? = null
    var sizeModel: MutableLiveData<SizeModel>? = null

    fun getVehicleBrand(): LiveData<VehicleBrandModel>? {
        return vehicleTypeBrandModel
    }

    fun getVehicleBrandModel(vehicle_type_id: String, accessToken: String, context: Context) {

        warrantyRepository = WarrantyRepository().getInstance()
        vehicleTypeBrandModel =
            warrantyRepository!!.getVehicleBrand(vehicle_type_id, accessToken, context)

    }

    fun getVehiclePattern(accessToken: String, context: Context) {

        warrantyRepository = WarrantyRepository().getInstance()
        PatternModel =
            warrantyRepository!!.getVehiclePattern(accessToken, context)

    }

    fun getVehiclePattern(): LiveData<PatternModel>? {
        return PatternModel!!
    }

    fun getVehicleSize(accessToken: String,  context: Context) {

        warrantyRepository = WarrantyRepository().getInstance()
        sizeModel =
            warrantyRepository!!.getVehicleSize(accessToken, context)
    }

    fun getVehicleSize(): LiveData<SizeModel>? {
        return sizeModel!!
    }


}
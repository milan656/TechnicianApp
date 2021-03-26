package com.walkins.technician.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jkadvantage.model.vehicleBrandModel.VehicleBrandModel
import com.walkins.technician.repository.WarrantyRepository

class WarrantyViewModel : ViewModel() {

    private var warrantyRepository: WarrantyRepository? = null
    var vehicleTypeBrandModel: MutableLiveData<VehicleBrandModel>? = null

    fun getVehicleBrand(): LiveData<VehicleBrandModel>? {
        return vehicleTypeBrandModel
    }

    fun getVehicleBrandModel(vehicle_type_id: String, accessToken: String, context: Context) {

        warrantyRepository = WarrantyRepository().getInstance()
        vehicleTypeBrandModel =
            warrantyRepository!!.getVehicleBrand(vehicle_type_id, accessToken, context)

    }


}
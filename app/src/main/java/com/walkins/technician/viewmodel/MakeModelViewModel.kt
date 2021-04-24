package com.walkins.technician.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.jkadvantage.model.vehicleBrandModel.VehicleBrandModel
import com.walkins.technician.model.login.makemodel.VehicleMakeModel
import com.walkins.technician.model.login.makemodel.VehicleModel
import com.walkins.technician.model.login.patternmodel.PatternModel
import com.walkins.technician.model.login.sizemodel.SizeModel
import com.walkins.technician.repository.MakeModelRepository
import com.walkins.technician.repository.WarrantyRepository

class MakeModelViewModel : ViewModel() {

    private var makeModelRepository: MakeModelRepository? = null
    private var makeModelClass: MutableLiveData<VehicleMakeModel>? = null
    private var modelclass: MutableLiveData<VehicleModel>? = null


    fun getVehicleMakeList(): LiveData<VehicleMakeModel>? {
        return makeModelClass!!
    }

    fun getVehicleMake(context: Context,accessToken: String) {

        makeModelRepository = MakeModelRepository().getInstance()
        makeModelClass =
            makeModelRepository!!.getVehicleMake(context,accessToken)

    }

    fun getVehicleModelList(): LiveData<VehicleModel>? {
        return modelclass!!
    }

    fun getVehicleModel(context: Context, makeId: Int,accessToken: String) {

        makeModelRepository = MakeModelRepository().getInstance()
        modelclass =
            makeModelRepository!!.getVehicleModel(makeId, accessToken,context)

    }


}
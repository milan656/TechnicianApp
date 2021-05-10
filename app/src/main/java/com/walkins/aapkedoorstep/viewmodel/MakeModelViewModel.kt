package com.walkins.aapkedoorstep.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.jkadvantage.model.vehicleBrandModel.VehicleBrandModel
import com.walkins.aapkedoorstep.model.login.building.BuildingListModel
import com.walkins.aapkedoorstep.model.login.makemodel.VehicleMakeModel
import com.walkins.aapkedoorstep.model.login.makemodel.VehicleModel
import com.walkins.aapkedoorstep.model.login.patternmodel.PatternModel
import com.walkins.aapkedoorstep.model.login.sizemodel.SizeModel
import com.walkins.aapkedoorstep.repository.MakeModelRepository
import com.walkins.aapkedoorstep.repository.WarrantyRepository

class MakeModelViewModel : ViewModel() {

    private var makeModelRepository: MakeModelRepository? = null
    private var makeModelClass: MutableLiveData<VehicleMakeModel>? = null
    private var buildingListModel: MutableLiveData<BuildingListModel>? = null
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
    fun callBuildingListApi(context: Context,accessToken: String) {

        makeModelRepository = MakeModelRepository().getInstance()
        buildingListModel =
            makeModelRepository!!.getBuilding(context,accessToken)

    }

    fun getBuildingModelList(): LiveData<BuildingListModel>? {
        return buildingListModel!!
    }

    fun getVehicleModel(context: Context, makeId: Int,accessToken: String) {

        makeModelRepository = MakeModelRepository().getInstance()
        modelclass =
            makeModelRepository!!.getVehicleModel(makeId, accessToken,context)

    }


}
package com.walkins.technician.DB

import androidx.room.*
import com.jkadvantage.model.vehicleBrandModel.Data

@Dao
interface DaoClass {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveVehicleType(book: VehicleMakeModelClass)

    @Query(value = "Select * from VehicleMakeModelClass")
    fun getAllVehicleType(): List<Data>

    @Query("delete from VehicleMakeModelClass")
    fun deleteAll()
}
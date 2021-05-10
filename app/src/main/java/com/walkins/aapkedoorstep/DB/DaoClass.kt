package com.walkins.aapkedoorstep.DB

import androidx.room.*
import com.jkadvantage.model.vehicleBrandModel.Data

@androidx.room.Dao
interface DaoClass {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveVehicleType(book: VehicleMakeModelClass)

    @Query(value = "Select * from vehiclemake")
    fun getAllVehicleType(): List<VehicleMakeModelClass>

    @Query("delete from vehiclemake")
    fun deleteAll()

    @Update
    fun update(book: VehicleMakeModelClass)


}
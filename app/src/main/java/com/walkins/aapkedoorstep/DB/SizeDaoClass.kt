package com.walkins.aapkedoorstep.DB

import androidx.room.*
import androidx.room.Dao
import com.jkadvantage.model.vehicleBrandModel.Data
import com.walkins.aapkedoorstep.model.login.sizemodel.SizeData

@androidx.room.Dao
interface SizeDaoClass {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSize(book: VehicleSizeModelClass)

    @Query(value = "Select * from vehiclesize")
    fun getAllSize(): List<VehicleSizeModelClass>

    @Query("delete from vehiclesize")
    fun deleteAll()

    @Update
    fun update(book: VehicleSizeModelClass)


}
package com.walkins.technician.DB

import androidx.room.*
import com.jkadvantage.model.vehicleBrandModel.Data
import com.walkins.technician.model.login.sizemodel.SizeData

@Dao
interface SizeDaoClass {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSize(book: VehicleSizeModelClass)

    @Query(value = "Select * from VehicleSizeModelClass")
    fun getAllSize(): List<SizeData>

    @Query("delete from VehicleSizeModelClass")
    fun deleteAll()

    @Update
    fun update(book: VehicleSizeModelClass)


}
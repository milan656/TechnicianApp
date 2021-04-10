package com.walkins.technician.DB

import androidx.room.*
import com.jkadvantage.model.vehicleBrandModel.Data
import com.walkins.technician.model.login.patternmodel.PatternData
import com.walkins.technician.model.login.sizemodel.SizeData

@Dao
interface PatternDaoClass {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePattern(book: VehiclePatternModelClass)

    @Query(value = "Select * from VehiclePatternModelClass")
    fun getAllPattern(): List<PatternData>

    @Query("delete from VehiclePatternModelClass")
    fun deleteAll()

    @Update
    fun update(book: VehiclePatternModelClass)


}
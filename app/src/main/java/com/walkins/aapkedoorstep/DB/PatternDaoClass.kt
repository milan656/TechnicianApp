package com.walkins.aapkedoorstep.DB

import androidx.room.*
import androidx.room.Dao
import com.jkadvantage.model.vehicleBrandModel.Data
import com.walkins.aapkedoorstep.model.login.patternmodel.PatternData
import com.walkins.aapkedoorstep.model.login.sizemodel.SizeData

@Dao
interface PatternDaoClass {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePattern(book: VehiclePatternModelClass)

    @Query(value = "Select * from vehiclepattern")
    fun getAllPattern(): List<VehiclePatternModelClass>

    @Query("delete from vehiclepattern")
    fun deleteAll()

    @Update
    fun update(book: VehiclePatternModelClass)



}
package com.walkins.aapkedoorstep.DB

import androidx.room.*
import androidx.room.Dao
import com.jkadvantage.model.vehicleBrandModel.Data
import com.walkins.aapkedoorstep.model.login.patternmodel.PatternData
import com.walkins.aapkedoorstep.model.login.sizemodel.SizeData

@Dao
interface ServiceListDaoClass {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(model: ServiceListModelClass)

    @Query(value = "Select * from servicelist")
    fun getAll(): List<ServiceListModelClass>

    @Query("delete from servicelist")
    fun deleteAll()

    @Update
    fun update(model: ServiceListModelClass)


}
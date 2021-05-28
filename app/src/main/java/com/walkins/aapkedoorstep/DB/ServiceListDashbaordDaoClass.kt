package com.walkins.aapkedoorstep.DB

import androidx.room.*
import androidx.room.Dao
import com.jkadvantage.model.vehicleBrandModel.Data
import com.walkins.aapkedoorstep.model.login.patternmodel.PatternData
import com.walkins.aapkedoorstep.model.login.sizemodel.SizeData

@Dao
interface ServiceListDashbaordDaoClass {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(model: ServiceDashboardModelClass)

    @Query(value = "Select * from servicelistdashboard")
    fun getAll(): List<ServiceDashboardModelClass>

    @Query("delete from servicelistdashboard")
    fun deleteAll()

    @Update
    fun update(model: ServiceDashboardModelClass)


}
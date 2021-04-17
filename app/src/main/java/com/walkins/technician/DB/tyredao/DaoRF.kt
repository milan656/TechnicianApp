package com.walkins.technician.DB.tyredao

import androidx.room.*
import com.walkins.technician.DB.TyreLFDetail
import com.walkins.technician.DB.TyreRFDetail
import com.walkins.technician.DB.VehiclePatternModelClass

@Dao
interface DaoRF {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(book: TyreRFDetail)

    @Query(value = "Select * from tyre_rf_detail")
    fun getAll(): List<TyreRFDetail>

    @Query("delete from tyre_rf_detail")
    fun deleteAll()

    @Update
    fun update(book: TyreRFDetail)


}
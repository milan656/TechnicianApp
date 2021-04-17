package com.walkins.technician.DB.tyredao

import androidx.room.*
import com.walkins.technician.DB.TyreLFDetail
import com.walkins.technician.DB.TyreRRDetail
import com.walkins.technician.DB.VehiclePatternModelClass

@Dao
interface DaoRR {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(book: TyreRRDetail)

    @Query(value = "Select * from tyre_rr_detail")
    fun getAll(): List<TyreRRDetail>

    @Query("delete from tyre_rr_detail")
    fun deleteAll()

    @Update
    fun update(book: TyreRRDetail)


}
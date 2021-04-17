package com.walkins.technician.DB.tyredao

import androidx.room.*
import com.walkins.technician.DB.TyreLFDetail
import com.walkins.technician.DB.VehiclePatternModelClass

@Dao
interface DaoLF {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(book: TyreLFDetail)

    @Query(value = "Select * from tyre_lf_detail")
    fun getAll(): List<TyreLFDetail>

    @Query("delete from tyre_lf_detail")
    fun deleteAll()

    @Update
    fun update(book: TyreLFDetail)


}
package com.walkins.technician.DB.tyredao

import androidx.room.*
import com.walkins.technician.DB.TyreLFDetail
import com.walkins.technician.DB.TyreLRDetail
import com.walkins.technician.DB.VehiclePatternModelClass

@Dao
interface DaoLR {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(book: TyreLRDetail)

    @Query(value = "Select * from tyre_lr_detail")
    fun getAll(): List<TyreLRDetail>

    @Query("delete from tyre_lr_detail")
    fun deleteAll()

    @Update
    fun update(book: TyreLRDetail)


}
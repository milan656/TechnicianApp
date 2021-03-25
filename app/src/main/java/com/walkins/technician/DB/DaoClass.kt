package com.walkins.technician.DB

import androidx.room.*

@Dao
interface DaoClass {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveVehicleType(book: EntityClass)

    @Query(value = "Select * from EntityClass")
    fun getAllVehicleType(): List<EntityClass>

    @Query("delete from EntityClass")
    fun deleteAll()
}
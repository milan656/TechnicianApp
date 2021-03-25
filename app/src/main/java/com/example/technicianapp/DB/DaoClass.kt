package com.example.walkinslatestapp.DB

import androidx.room.*

@Dao
interface DaoClass {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveRecord(book: EntityClass)

    @Query(value = "Select * from EntityClass")
    fun getAllRecord(): List<EntityClass>

    @Query("delete from EntityClass")
    fun deleteAll()
}
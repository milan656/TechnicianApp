package com.walkins.technician.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [(VehicleMakeModelClass::class), (VehiclePatternModelClass::class), (VehicleSizeModelClass::class)],
    version = 2,
    exportSchema = false
)
abstract class DBClass : RoomDatabase() {

    abstract fun daoClass(): DaoClass
    abstract fun sizeDaoClass(): SizeDaoClass
    abstract fun patternDaoClass(): PatternDaoClass

    companion object {
        private var INSTANCE: DBClass? = null
        fun getInstance(context: Context): DBClass {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    DBClass::class.java,
                    "roomdb"
                )
                    .build()
            }
            return INSTANCE as DBClass
        }
    }
}
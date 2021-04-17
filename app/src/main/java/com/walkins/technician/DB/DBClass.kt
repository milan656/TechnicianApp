package com.walkins.technician.DB

import android.content.Context
import androidx.room.*
import com.walkins.technician.DB.tyredao.DaoLF
import com.walkins.technician.DB.tyredao.DaoLR
import com.walkins.technician.DB.tyredao.DaoRF
import com.walkins.technician.DB.tyredao.DaoRR

@Database(
    entities = [(VehicleMakeModelClass::class),
        (VehiclePatternModelClass::class),
        (VehicleSizeModelClass::class),
        (TyreLFDetail::class),
        (TyreLRDetail::class),
        (TyreRFDetail::class),
        (TyreRRDetail::class)],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class DBClass : RoomDatabase() {

    abstract fun daoClass(): DaoClass
    abstract fun sizeDaoClass(): SizeDaoClass
    abstract fun patternDaoClass(): PatternDaoClass

    abstract fun daoLF(): DaoLF
    abstract fun daoLR(): DaoLR
    abstract fun daoRF(): DaoRF
    abstract fun daoRR(): DaoRR

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
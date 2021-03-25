package com.example.walkinslatestapp.DB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class EntityClass {

    @PrimaryKey(autoGenerate = true)
    var Id: Int = 0

    var vehicle_type_id: String = ""

    @ColumnInfo(name = "name")
    var name: String = ""

    @ColumnInfo(name = "type")
    var type: String = ""

    @ColumnInfo(name = "image_url")
    var image_url: String = ""
}
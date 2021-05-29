package com.walkins.aapkedoorstep.DB

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.walkins.aapkedoorstep.model.login.servicelistmodel.ServiceListData
import java.lang.reflect.Type


class Converters {
    @TypeConverter
    fun fromString(value: String?): ArrayList<String?>? {
        val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.getType()
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    open fun fromOptionValuesList(optionValues: List<ServiceListData?>?): String? {
        if (optionValues == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<ServiceListData?>?>() {}.type
        return gson.toJson(optionValues, type)
    }

    @TypeConverter // note this annotation
    fun toOptionValuesList(optionValuesString: String?): List<ServiceListData>? {
        if (optionValuesString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<ServiceListData?>?>() {}.type
        return gson.fromJson<List<ServiceListData>>(optionValuesString, type)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun getListfromString(id: String): List<Int> {
        val list: MutableList<Int> = ArrayList()

        val array: List<String> = id.split(",")

        for (s in array) {
            if (!s.isEmpty()) {
                list.add(s.toInt())
            }
        }
        return list
    }

    @TypeConverter
    fun writeStringfromList(list: List<Int?>?): String {
        var genreIds = ""
        for (i in list!!) {
            genreIds += ",$i"
        }
        return genreIds
    }
}
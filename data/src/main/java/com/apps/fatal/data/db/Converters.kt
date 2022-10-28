package com.apps.fatal.data.db

import androidx.room.TypeConverter
import com.apps.fatal.data.common.fromJson
import com.apps.fatal.data.models.CurrentData
import com.google.gson.Gson

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun currentDataToStr(value: CurrentData?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun strToCurrentData(value: String?): CurrentData? {
        if (value == null) return null
        return gson.fromJson(value)
    }
}
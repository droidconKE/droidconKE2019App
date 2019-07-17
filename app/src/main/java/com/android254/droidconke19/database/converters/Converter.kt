package com.android254.droidconke19.database.converters

import androidx.room.TypeConverter
import com.android254.droidconke19.models.Level
import com.android254.droidconke19.models.Stage
import com.android254.droidconke19.models.Type
import com.android254.droidconke19.utils.enumToInt
import com.android254.droidconke19.utils.toEnum
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class Converter {
    @TypeConverter
    fun fromString(value: String): ArrayList<Int>? {
        val listType = object : TypeToken<ArrayList<Int>>() {

        }.type
        return Gson().fromJson<ArrayList<Int>>(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<Int>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun listFromString(value: String): ArrayList<String>? {
        val listType = object : TypeToken<ArrayList<String>>() {

        }.type
        return Gson().fromJson<ArrayList<String>>(value, listType)
    }

    @TypeConverter
    fun listFromArrayList(list: ArrayList<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun typeEnumToInt(type : Type) = type.enumToInt()

    @TypeConverter
    fun intEnumToType(value : Int) = value.toEnum<Type>()

    @TypeConverter
    fun levelEnumToInt(level : Level) = level.enumToInt()

    @TypeConverter
    fun intEnumToLevel(value : Int) = value.toEnum<Level>()

    @TypeConverter
    fun stageEnumToInt(stage : Stage) = stage.enumToInt()

    @TypeConverter
    fun intEnumToStage(value : Int) = value.toEnum<Stage>()


}

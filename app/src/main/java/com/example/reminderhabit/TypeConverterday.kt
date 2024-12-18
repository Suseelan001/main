package com.example.reminderhabit

import androidx.room.TypeConverter

class TypeConverterday {

    @TypeConverter
    fun fromDaysList(days: ArrayList<String>?): String? {
        return days?.joinToString(",")
    }

    @TypeConverter
    fun toDaysList(data: String?): ArrayList<String>? {
        return data?.split(",")?.let { ArrayList(it) }
    }
}

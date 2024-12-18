package com.example.reminderhabit.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.reminderhabit.TypeConverterday
import java.util.ArrayList

@Entity(tableName = "tracker_dao")

data class TrackerModel(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val title: String,
    val description: String,
    @TypeConverters(TypeConverterday::class)
    val days: ArrayList<String>,
    val color: String,
    val startTime: String,
    val endTime: String,
    val type: String ="Tracker",
    val isNotificationEnabled: Boolean,
    val selectedDate: String? = null)


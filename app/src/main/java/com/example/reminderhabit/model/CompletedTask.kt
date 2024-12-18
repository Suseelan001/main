package com.example.reminderhabit.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.reminderhabit.TypeConverterday
import java.util.ArrayList

@Entity(tableName = "completed_task_dao")

data class CompletedTask(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val title: String,
    val description: String,
    @TypeConverters(TypeConverterday::class)
    val days: ArrayList<String>,
    val color: String,
    val startTime: String,
    val endTime: String,
    val type: String ,
    val isNotificationEnabled: Boolean,
    val createdDate: String? = null)


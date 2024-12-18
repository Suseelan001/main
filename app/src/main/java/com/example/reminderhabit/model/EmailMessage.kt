package com.example.reminderhabit.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.reminderhabit.TypeConverterday
import java.util.ArrayList

data class EmailMessage(
    val sender: String,
    val message: String
)

package com.example.reminderhabit.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.reminderhabit.TypeConverterday
import com.example.reminderhabit.model.AddTask
import com.example.reminderhabit.model.CompletedTask
import com.example.reminderhabit.model.SkippedTask
import com.example.reminderhabit.model.TrackerModel
import com.example.reminderhabit.model.UserDetail


@Database(entities = [UserDetail::class,AddTask::class, TrackerModel::class, SkippedTask::class, CompletedTask::class],   exportSchema = false,version = 14)
@TypeConverters(TypeConverterday::class)
abstract class AppRoomDataBase: RoomDatabase() {
    abstract fun userDAO(): UserDAO
    abstract fun taskDAO(): TaskDAO
    abstract fun trackerDAO(): TrackerDAO
    abstract fun skippedTaskDAO(): SkippedTaskDAO
    abstract fun completedTaskDAO(): CompletedTaskDAO
}
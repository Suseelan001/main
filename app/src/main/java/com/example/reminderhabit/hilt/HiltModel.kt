package com.example.reminderhabit.hilt

import android.content.Context
import androidx.room.Room
import com.example.reminderhabit.database.AppRoomDataBase
import com.example.reminderhabit.database.CompletedTaskDAO
import com.example.reminderhabit.database.SkippedTaskDAO
import com.example.reminderhabit.database.TaskDAO
import com.example.reminderhabit.database.TrackerDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HiltModel {

    @Provides
    @Singleton
    fun providerContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideDB(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, AppRoomDataBase::class.java, "REMINDERDB"
    )
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideUserDAO(db: AppRoomDataBase) = db.userDAO()

    @Provides
    @Singleton
    fun provideTaskDAO(db: AppRoomDataBase): TaskDAO = db.taskDAO()

    @Provides
    @Singleton
    fun provideTrackerDAO(db: AppRoomDataBase): TrackerDAO = db.trackerDAO()


    @Provides
    @Singleton
    fun provideSkippedTaskDAO(db: AppRoomDataBase): SkippedTaskDAO = db.skippedTaskDAO()

    @Provides
    @Singleton
    fun provideCompletedTaskDAO(db: AppRoomDataBase): CompletedTaskDAO = db.completedTaskDAO()


}

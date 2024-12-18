package com.example.reminderhabit.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.reminderhabit.model.TrackerModel


@Dao
interface TrackerDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracker(task: TrackerModel)

    @Query("SELECT * FROM tracker_dao")
    fun getAllTrackers(): LiveData<List<TrackerModel>>


    @Query("DELETE FROM tracker_dao")
    suspend fun cleaTrackerDB()


    @Query("SELECT * FROM tracker_dao WHERE selectedDate <= :date ORDER BY selectedDate ASC")
    fun getTrackersFromDate(date: String): LiveData<List<TrackerModel>>


}
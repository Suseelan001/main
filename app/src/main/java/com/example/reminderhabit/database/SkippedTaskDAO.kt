package com.example.reminderhabit.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.reminderhabit.model.SkippedTask


@Dao
interface SkippedTaskDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: SkippedTask)

    @Update
    suspend fun updateRecord(task: SkippedTask)


    @Query("SELECT * FROM skipped_task_dao WHERE  createdDate >= :selectedDate AND  createdDate >= :endDate ORDER BY createdDate ASC")
    fun getAllRecord(selectedDate: String, endDate: String): LiveData<List<SkippedTask>>




    @Query("SELECT * FROM skipped_task_dao WHERE id = :id")
    fun getSingleRecord(id: Int): LiveData<SkippedTask>

    @Query("DELETE FROM skipped_task_dao")
    suspend fun clearUserDB()

    @Query("DELETE FROM skipped_task_dao WHERE id = :id")
    suspend fun deleteSingleRecord(id: Int)




}
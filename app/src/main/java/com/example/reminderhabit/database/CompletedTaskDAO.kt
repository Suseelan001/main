package com.example.reminderhabit.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.reminderhabit.model.CompletedTask


@Dao
interface CompletedTaskDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: CompletedTask)

    @Update
    suspend fun updateRecord(task: CompletedTask)


    @Query("SELECT * FROM completed_task_dao")
    fun getAllRecord(): LiveData<List<CompletedTask>>

    @Query("SELECT * FROM completed_task_dao WHERE id = :id")
    fun getSingleRecord(id: Int): LiveData<CompletedTask>

    @Query("DELETE FROM completed_task_dao")
    suspend fun clearUserDB()

    @Query("DELETE FROM completed_task_dao WHERE id = :id")
    suspend fun deleteSingleRecord(id: Int)




}
package com.example.reminderhabit.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.reminderhabit.model.AddTask


@Dao
interface TaskDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: AddTask)

    @Update
    suspend fun updateRecord(task: AddTask)


    @Query("SELECT * FROM add_task_dao")
    fun getAllRecord(): LiveData<List<AddTask>>

    @Query("SELECT * FROM add_task_dao WHERE id = :id")
    fun getSingleRecord(id: Int): LiveData<AddTask>


    @Query("UPDATE add_task_dao SET createdDate = :newCreatedDate WHERE id = :id")
    suspend fun updateCreatedDate(id: Int, newCreatedDate: String)



    @Query("DELETE FROM add_task_dao")
    suspend fun clearUserDB()

    @Query("DELETE FROM add_task_dao WHERE id = :id")
    suspend fun deleteSingleRecord(id: Int)

    @Query("SELECT * FROM add_task_dao WHERE type = :type AND createdDate <= :date ORDER BY createdDate ASC")
    fun getTasksFromDate(type: String, date: String): LiveData<List<AddTask>>

    @Query("SELECT * FROM add_task_dao WHERE  createdDate >= :date ORDER BY createdDate ASC")
    fun getUpcomingList( date: String): LiveData<List<AddTask>>

    @Query("SELECT * FROM add_task_dao WHERE  createdDate <= :date  AND startTime >= :startTime  ORDER BY createdDate ASC")
    fun getListFromTimeAndDate( startTime:String ,date: String): LiveData<List<AddTask>>


    @Query("SELECT * FROM add_task_dao WHERE  createdDate <= :date  AND startTime < :startTime  ORDER BY createdDate ASC")
    fun getBackLogList( startTime:String ,date: String): LiveData<List<AddTask>>



}
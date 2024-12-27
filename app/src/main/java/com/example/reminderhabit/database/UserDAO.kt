package com.example.reminderhabit.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.reminderhabit.model.SkippedTask
import com.example.reminderhabit.model.UserDetail


@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserDetail)

    @Update
    fun updateUser(user: UserDetail)

    @Query("SELECT * FROM user_details WHERE email = :email")
    suspend fun findUser(email: String): UserDetail?

    @Query("SELECT * FROM user_details")
    suspend fun getAllUsers(): List<UserDetail>


    @Query("DELETE FROM user_details")
    suspend fun clearUserDB()



    @Query("SELECT * FROM user_details WHERE email = :email")
     fun getUser(email: String): LiveData<UserDetail>

    @Query("UPDATE user_details SET password = :newpassword WHERE id = :id")
    suspend fun updatePassword(id: Int, newpassword: String)


}
package com.example.reminderhabit.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.reminderhabit.model.UserDetail


@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserDetail)

    @Query("SELECT * FROM user_details WHERE email = :email")
    suspend fun findUser(email: String): UserDetail?

    @Query("SELECT * FROM user_details")
    suspend fun getAllUsers(): List<UserDetail>


    @Query("DELETE FROM user_details")
    suspend fun clearUserDB()
}
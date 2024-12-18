package com.example.reminderhabit.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_details")
data class UserDetail(
    @PrimaryKey(autoGenerate = true)

    val id:Int=0,
    val name: String,
    val email:String,
    val password:String,
    val profileImage:String? = null)

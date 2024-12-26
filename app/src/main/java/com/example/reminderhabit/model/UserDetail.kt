package com.example.reminderhabit.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_details")
data class UserDetail(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val name: String,
    val password:String="",
    val email:String,
    val profileImage:String? = "",
    val phoneNumber:String?="",)

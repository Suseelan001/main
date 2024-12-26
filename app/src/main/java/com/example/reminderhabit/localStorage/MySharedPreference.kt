package com.example.reminderhabit.localStorage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import javax.inject.Inject

class MySharedPreference @Inject constructor(context: Context) {

    private val sharedPreference: SharedPreferences =
        context.getSharedPreferences("REMINDER_HABIT", MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreference.edit()

    fun setLoggedIn(loginStatus:Boolean) {
        editor.putBoolean("Is_LoggedIn", loginStatus)
        editor.commit()
    }

    fun getLoggedIn(): Boolean {
        return sharedPreference.getBoolean("Is_LoggedIn", false)
    }

   fun setUserMailId(loginStatus:String) {
        editor.putString("User_Mail", loginStatus)
        editor.commit()
    }

    fun getUserMailId(): String? {
        return sharedPreference.getString("User_Mail", "")
    }


    fun clearAll() {
        editor.putBoolean("Is_LoggedIn", false)
        editor.putString("User_Mail", "")

        editor.commit()
    }



}
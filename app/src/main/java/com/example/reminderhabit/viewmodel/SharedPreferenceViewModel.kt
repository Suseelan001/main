package com.example.reminderhabit.viewmodel

import androidx.lifecycle.ViewModel
import javax.inject.Inject
import com.example.reminderhabit.localStorage.MySharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class SharedPreferenceViewModel @Inject constructor(private val mySharedPreference: MySharedPreference) : ViewModel() {

    fun getUserLoggedIn(): Boolean {
        return mySharedPreference.getLoggedIn()
    }

    fun setLoggedIn(loginStatus:Boolean){
        mySharedPreference.setLoggedIn(loginStatus)
    }


    fun getUserMailId(): String? {
        return mySharedPreference.getUserMailId()
    }

    fun setUserMailId(userMail:String){
        mySharedPreference.setUserMailId(userMail)
    }



}

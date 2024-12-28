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


    fun getUserMobileNumber(): String? {
        return mySharedPreference.getUserMobileNumber()
    }

    fun setUserMobileNumber(userMobileNumber:String){
        mySharedPreference.setUserMobileNumber(userMobileNumber)
    }



}

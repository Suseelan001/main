package com.example.reminderhabit.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.reminderhabit.model.UserDetail
import com.example.reminderhabit.repository.UserDatabaseRepository
import javax.inject.Inject
import androidx.compose.runtime.State
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userDatabaseRepository: UserDatabaseRepository
) : ViewModel() {


    private val _userDetail = MutableLiveData<UserDetail?>()
    val userDetail: LiveData<UserDetail?> get() = _userDetail

    fun insertUser(user: UserDetail) {
        viewModelScope.launch {
            userDatabaseRepository.insertUser(user)
        }
    }

    fun getUserDetail(email: String) {
        viewModelScope.launch {
            _userDetail.value = userDatabaseRepository.getUserDetail(email)
        }
    }



    fun clearUserData() {
        viewModelScope.launch {
            userDatabaseRepository.clearUserDB()
            _userDetail.value = null
        }
    }
}

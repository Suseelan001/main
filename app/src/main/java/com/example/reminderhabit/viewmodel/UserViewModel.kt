package com.example.reminderhabit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.reminderhabit.model.UserDetail
import com.example.reminderhabit.repository.UserDatabaseRepository
import javax.inject.Inject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.reminderhabit.localStorage.MySharedPreference
import com.example.reminderhabit.repository.CompletedTaskDatabaseRepository
import com.example.reminderhabit.repository.SkippedTaskDatabaseRepository
import com.example.reminderhabit.repository.TaskDatabaseRepository
import com.example.reminderhabit.repository.TrackerDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class UserViewModel @Inject constructor(
    private val mySharedPreference: MySharedPreference,
    private val userDatabaseRepository: UserDatabaseRepository,
    private val completedTaskDatabaseRepository: CompletedTaskDatabaseRepository,
    private val skippedTaskDatabaseRepository: SkippedTaskDatabaseRepository,
    private val taskDatabaseRepository: TaskDatabaseRepository,

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

    fun isEmailUsed(email: String): LiveData<Boolean> {
        val isUsed = MutableLiveData<Boolean>()
        viewModelScope.launch {
            isUsed.value = userDatabaseRepository.getUserDetail(email) != null
        }
        return isUsed
    }


    fun clearUserData() {
        viewModelScope.launch {
            userDatabaseRepository.clearUserDB()
            _userDetail.value = null
        }
    }

    fun clearUserDetail() {
        viewModelScope.launch {
            _userDetail.value = null
        }
    }


    fun clearData() = viewModelScope.launch {
        mySharedPreference.clearAll()
        userDatabaseRepository.clearUserDB()
        completedTaskDatabaseRepository.clearUserDB()
        skippedTaskDatabaseRepository.clearUserDB()
        taskDatabaseRepository.clearUserDB()

    }

}

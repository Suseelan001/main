package com.example.reminderhabit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.reminderhabit.model.AddTask
import com.example.reminderhabit.repository.TaskDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val userDatabaseRepository: TaskDatabaseRepository
) : ViewModel() {

    fun insertTask(task: AddTask) {
        viewModelScope.launch {
            userDatabaseRepository.insertTask(task)
        }
    }
    fun updateRecord(task: AddTask) {
        viewModelScope.launch {
            userDatabaseRepository.updateRecord(task)
        }
    }


    fun updateCreatedDate(id: Int,createdDate:String) {
        viewModelScope.launch {
            userDatabaseRepository.updateCreatedDate(id,createdDate)
        }
    }


    fun clearUserDB() {
        viewModelScope.launch {
            userDatabaseRepository.clearUserDB()
        }
    }

    fun getSingleRecord(id:Int): LiveData<AddTask> {
        return userDatabaseRepository.getSingleRecord(id)
    }

    fun deleteSingleRecord(id:Int) {
        viewModelScope.launch {
            userDatabaseRepository.deleteSingleRecord(id)

        }
    }

    fun getTasksFromDate(type:String,selectedDate: String): LiveData<List<AddTask>> {
        return userDatabaseRepository.getTasksFromDate(type,selectedDate)
    }


    fun getUpcomingList(selectedDate: String): LiveData<List<AddTask>> {
        return userDatabaseRepository.getUpcomingList(selectedDate)
    }


    fun getListFromTimeAndDate(startTime:String,selectedDate: String): LiveData<List<AddTask>> {
        return userDatabaseRepository.getListFromTimeAndDate(startTime,selectedDate)
    }


    fun getBackLogList(startTime:String,selectedDate: String): LiveData<List<AddTask>> {
        return userDatabaseRepository.getBackLogList(startTime,selectedDate)
    }


    fun getAllRecord(): LiveData<List<AddTask>> {
        return userDatabaseRepository.getAllRecord()
    }


}

package com.example.reminderhabit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.reminderhabit.model.SkippedTask
import com.example.reminderhabit.repository.SkippedTaskDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class SkippedTaskViewModel @Inject constructor(
    private val skippedTaskDatabaseRepository: SkippedTaskDatabaseRepository
) : ViewModel() {

    fun insertTask(task: SkippedTask) {
        viewModelScope.launch {
            skippedTaskDatabaseRepository.insertTask(task)
        }
    }
    fun updateRecord(task: SkippedTask) {
        viewModelScope.launch {
            skippedTaskDatabaseRepository.updateRecord(task)
        }
    }


    fun clearUserDB() {
        viewModelScope.launch {
            skippedTaskDatabaseRepository.clearUserDB()
        }
    }

    fun getSingleRecord(id:Int): LiveData<SkippedTask> {
        return skippedTaskDatabaseRepository.getSingleRecord(id)
    }

    fun deleteSingleRecord(id:Int) {
        viewModelScope.launch {
            skippedTaskDatabaseRepository.deleteSingleRecord(id)

        }
    }


    fun getAllRecord(selectedDate: String, endDate: String): LiveData<List<SkippedTask>> {
        return skippedTaskDatabaseRepository.getAllRecord(selectedDate,endDate)
    }


}

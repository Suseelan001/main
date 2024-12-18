package com.example.reminderhabit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.reminderhabit.model.CompletedTask
import com.example.reminderhabit.repository.CompletedTaskDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class CompletedTaskViewModel @Inject constructor(
    private val completedTaskDatabaseRepository: CompletedTaskDatabaseRepository
) : ViewModel() {

    fun insertTask(task: CompletedTask) {
        viewModelScope.launch {
            completedTaskDatabaseRepository.insertTask(task)
        }
    }
    fun updateRecord(task: CompletedTask) {
        viewModelScope.launch {
            completedTaskDatabaseRepository.updateRecord(task)
        }
    }


    fun clearUserDB() {
        viewModelScope.launch {
            completedTaskDatabaseRepository.clearUserDB()
        }
    }

    fun getSingleRecord(id:Int): LiveData<CompletedTask> {
        return completedTaskDatabaseRepository.getSingleRecord(id)
    }

    fun deleteSingleRecord(id:Int) {
        viewModelScope.launch {
            completedTaskDatabaseRepository.deleteSingleRecord(id)

        }
    }


    fun getAllRecord(): LiveData<List<CompletedTask>> {
        return completedTaskDatabaseRepository.getAllRecord()
    }


}

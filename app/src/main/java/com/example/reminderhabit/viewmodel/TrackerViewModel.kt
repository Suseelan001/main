package com.example.reminderhabit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.reminderhabit.model.AddTask
import com.example.reminderhabit.model.TrackerModel
import com.example.reminderhabit.repository.TaskDatabaseRepository
import com.example.reminderhabit.repository.TrackerDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class TrackerViewModel @Inject constructor(
    private val trackerDatabaseRepository: TrackerDatabaseRepository
) : ViewModel() {

    fun insertTask(task: TrackerModel) {
        viewModelScope.launch {
            trackerDatabaseRepository.insertTask(task)
        }
    }

    fun getTasksFromDate(selectedDate: String): LiveData<List<TrackerModel>> {
        return trackerDatabaseRepository.getTasksFromDate(selectedDate)
    }


}

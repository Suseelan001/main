package com.example.reminderhabit.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.reminderhabit.work.ReminderWorker


class ReminderViewModel(application: Application): ViewModel() {


    private val workManager = WorkManager.getInstance(application)

    internal fun scheduleReminder(
    ) {
        // create a Data instance with the plantName passed to it
        val myWorkRequestBuilder = OneTimeWorkRequestBuilder<ReminderWorker>()
        workManager.enqueue(myWorkRequestBuilder.build())
    }
}

class ReminderViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ReminderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            ReminderViewModel(application) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
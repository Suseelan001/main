package com.example.reminderhabit.repository




import androidx.lifecycle.LiveData
import com.example.reminderhabit.model.TrackerModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.reminderhabit.database.TrackerDAO
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class TrackerDatabaseRepository @Inject constructor(
    private val trackerDAO: TrackerDAO
) {

    suspend fun insertTask(task: TrackerModel) {
        withContext(Dispatchers.IO) {
            trackerDAO.insertTracker(task)
        }
    }

    fun getAllTaskList(): LiveData<List<TrackerModel>> {
        return trackerDAO.getAllTrackers()
    }

    fun getTasksFromDate(date: String): LiveData<List<TrackerModel>> {
        return trackerDAO.getTrackersFromDate(date)
    }
}

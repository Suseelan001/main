package com.example.reminderhabit.repository




import androidx.lifecycle.LiveData
import com.example.reminderhabit.database.SkippedTaskDAO
import com.example.reminderhabit.model.SkippedTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class SkippedTaskDatabaseRepository @Inject constructor(
    private val skippedTaskDAO: SkippedTaskDAO
) {

    suspend fun insertTask(task: SkippedTask) {
        withContext(Dispatchers.IO) {
            skippedTaskDAO.insertTask(task)
        }
    }

    suspend fun updateRecord(task: SkippedTask) {
        withContext(Dispatchers.IO) {
            skippedTaskDAO.updateRecord(task)
        }
    }

    suspend fun clearUserDB() {
        withContext(Dispatchers.IO) {
            skippedTaskDAO.clearUserDB()
        }
    }
    suspend fun deleteSingleRecord(id: Int) {
        withContext(Dispatchers.IO) {
            skippedTaskDAO.deleteSingleRecord(id)
        }
    }



    fun getSingleRecord(id: Int): LiveData<SkippedTask> {
        return skippedTaskDAO.getSingleRecord(id)
    }


    fun getAllRecord(selectedDate: String, endDate: String): LiveData<List<SkippedTask>> {
        return skippedTaskDAO.getAllRecord(selectedDate ,endDate)
    }


}

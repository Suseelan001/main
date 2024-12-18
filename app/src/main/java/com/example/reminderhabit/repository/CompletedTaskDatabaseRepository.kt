package com.example.reminderhabit.repository




import androidx.lifecycle.LiveData
import com.example.reminderhabit.database.CompletedTaskDAO
import com.example.reminderhabit.model.CompletedTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class CompletedTaskDatabaseRepository @Inject constructor(
    private val completedTaskDAO: CompletedTaskDAO
) {

    suspend fun insertTask(task: CompletedTask) {
        withContext(Dispatchers.IO) {
            completedTaskDAO.insertTask(task)
        }
    }

    suspend fun updateRecord(task: CompletedTask) {
        withContext(Dispatchers.IO) {
            completedTaskDAO.updateRecord(task)
        }
    }

    suspend fun clearUserDB() {
        withContext(Dispatchers.IO) {
            completedTaskDAO.clearUserDB()
        }
    }
    suspend fun deleteSingleRecord(id: Int) {
        withContext(Dispatchers.IO) {
            completedTaskDAO.deleteSingleRecord(id)
        }
    }



    fun getSingleRecord(id: Int): LiveData<CompletedTask> {
        return completedTaskDAO.getSingleRecord(id)
    }


    fun getAllRecord(): LiveData<List<CompletedTask>> {
        return completedTaskDAO.getAllRecord( )
    }


}

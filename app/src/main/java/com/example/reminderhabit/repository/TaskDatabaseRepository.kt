package com.example.reminderhabit.repository




import androidx.lifecycle.LiveData
import com.example.reminderhabit.database.TaskDAO
import com.example.reminderhabit.model.AddTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class TaskDatabaseRepository @Inject constructor(
    private val taskDao: TaskDAO
) {

    suspend fun insertTask(task: AddTask) {
        withContext(Dispatchers.IO) {
            taskDao.insertTask(task)
        }
    }

    suspend fun updateRecord(task: AddTask) {
        withContext(Dispatchers.IO) {
            taskDao.updateRecord(task)
        }
    }

    suspend fun updateCreatedDate(id: Int,createdDate:String) {
        withContext(Dispatchers.IO) {
            taskDao.updateCreatedDate(id,createdDate)
        }
    }

    suspend fun clearUserDB() {
        withContext(Dispatchers.IO) {
            taskDao.clearUserDB()
        }
    }
    suspend fun deleteSingleRecord(id: Int) {
        withContext(Dispatchers.IO) {
            taskDao.deleteSingleRecord(id)
        }
    }




    fun getSingleRecord(id: Int): LiveData<AddTask> {
        return taskDao.getSingleRecord(id)
    }

    fun getTasksFromDate(type:String,date: String): LiveData<List<AddTask>> {
        return taskDao.getTasksFromDate(type,date)
    }

    fun getUpcomingList(date: String): LiveData<List<AddTask>> {
        return taskDao.getUpcomingList(date)
    }

    fun getListFromTimeAndDate(startTime:String,date: String): LiveData<List<AddTask>> {
        return taskDao.getListFromTimeAndDate(startTime,date)
    }

    fun getBackLogList(startTime:String,date: String): LiveData<List<AddTask>> {
        return taskDao.getBackLogList(startTime,date)
    }

    fun getAllRecord(): LiveData<List<AddTask>> {
        return taskDao.getAllRecord( )
    }


}

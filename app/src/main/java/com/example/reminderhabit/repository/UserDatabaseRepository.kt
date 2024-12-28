package com.example.reminderhabit.repository



import androidx.lifecycle.LiveData
import com.example.reminderhabit.database.UserDAO
import com.example.reminderhabit.model.UserDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class UserDatabaseRepository @Inject constructor(
    private val userDao: UserDAO
) {

    suspend fun insertUser(user: UserDetail) {
        withContext(Dispatchers.IO) {
            userDao.insertUser(user)
        }
    }

    suspend fun updateUser(user: UserDetail) {
        withContext(Dispatchers.IO) {
            userDao.updateUser(user)
        }
    }

    suspend fun getUserDetail(phoneNumber: String): UserDetail? {
        return withContext(Dispatchers.IO) {
            userDao.findUser(phoneNumber)
        }
    }
     fun getUser(userMobileNumber: String): LiveData<UserDetail> {
        return userDao.getUser(userMobileNumber)
    }

    suspend fun updatePassword(id: Int,newPassword:String) {
        withContext(Dispatchers.IO) {
            userDao.updatePassword(id,newPassword)
        }
    }

    suspend fun getAllUsers():List<UserDetail>   {
        return withContext(Dispatchers.IO) {
            userDao.getAllUsers()
        }
    }
    suspend fun clearUserDB() {
        withContext(Dispatchers.IO) {
            userDao.clearUserDB()
        }
    }
}

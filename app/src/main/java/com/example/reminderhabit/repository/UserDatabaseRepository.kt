package com.example.reminderhabit.repository



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

    suspend fun getUserDetail(email: String): UserDetail? {
        return withContext(Dispatchers.IO) {
            userDao.findUser(email)
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

package com.synowkrz.housemanager.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.synowkrz.housemanager.babyTask.model.BabyProfile
import com.synowkrz.housemanager.babyTask.model.Feeding
import com.synowkrz.housemanager.database.HouseManagerDatabase
import com.synowkrz.housemanager.model.TaskGridItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.SQLException

class HouseRepository(private val app: Application) {

    private val database = HouseManagerDatabase.getInstance(app)
    val items by lazy { database.taskItemDao.getAllTasks() }
    val babies by lazy { database.babyProfileDao.getAllBabies()}

    suspend fun insertBabyProfile(babyProfile: BabyProfile) {
        withContext(Dispatchers.IO) {
            try {
                database.babyProfileDao.insert(babyProfile)
            } catch (e: Exception) {
                TODO()
            }
        }
    }

    suspend fun insertTask(taskGridItem: TaskGridItem) {
        withContext(Dispatchers.IO) {
            try {
                database.taskItemDao.insert(taskGridItem)
            } catch (e: SQLException) {
                TODO("Handle duplicate insert")
                // insert fail
            }
        }
    }

    suspend fun removeTask(taskGridItem: TaskGridItem) {
        withContext(Dispatchers.IO) {
            database.taskItemDao.deleteTask(taskGridItem.name)
        }
    }

    suspend fun removeBabyProfile(babyProfile: BabyProfile) {
        withContext(Dispatchers.IO) {
            database.babyProfileDao.deleteBaby(babyProfile.name)
        }
    }

    suspend fun getAllBabiesData() : List<BabyProfile> {
        return withContext(Dispatchers.IO) {
             database.babyProfileDao.getAllBabiesData()
        }
    }

    suspend fun getBabyProfile(name: String) : BabyProfile? {
        return withContext(Dispatchers.IO) {
            database.babyProfileDao.getBabyByName(name)
        }
    }

    suspend fun insertFeeding(feeding: Feeding) {
        withContext(Dispatchers.IO) {
            database.feedingDao.insert(feeding)
        }
    }

    fun getAllFeedings() : LiveData<List<Feeding>> {
        return database.feedingDao.getAllFeedings()
    }

    fun getAllFeedingsByName(name: String) : LiveData<List<Feeding>> {
        return database.feedingDao.getAllFeedingsByName(name)
    }
}
package com.synowkrz.housemanager.repository

import android.app.Application
import com.synowkrz.housemanager.database.HouseManagerDatabase
import com.synowkrz.housemanager.model.TaskGridItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.SQLException

class HouseRepository(private val app: Application) {

    private val database = HouseManagerDatabase.getInstance(app)
    val items by lazy { database.taskItemDao.getAllTasks() }
    val babies by lazy { database.babyProfileDao.getAllBabies()}

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
}
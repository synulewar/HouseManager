package com.synowkrz.housemanager.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.synowkrz.housemanager.homeTaskList.model.HomeTask

@Dao
interface HomeTaskDao {

    @Insert
    fun insert(homeTask: HomeTask)

    @Update
    fun update(homeTask: HomeTask)

    @Delete
    fun delete(homeTask: HomeTask)

    @Query("SELECT * FROM home_task ORDER BY daysExceeded DESC")
    fun getAllHomeTasks() : LiveData<List<HomeTask>>

    @Query("SELECT * FROM home_task")
    fun getAllTaskAsync() : List<HomeTask>

    @Query("SELECT * FROM home_task WHERE name = :name")
    fun getTaskByName(name : String) : HomeTask?
}
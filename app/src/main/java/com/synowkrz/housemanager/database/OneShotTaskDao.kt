package com.synowkrz.housemanager.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.synowkrz.housemanager.homeTaskList.model.OneShotTask

@Dao
interface OneShotTaskDao {

    @Insert
    fun insert(oneShotTask: OneShotTask)

    @Update
    fun update(oneShotTask: OneShotTask)

    @Delete
    fun delete(oneShotTask: OneShotTask)

    @Query("SELECT * FROM one_shot_home_task WHERE done = 0 ORDER BY dayExceeded DESC")
    fun getAllTaskToDo() : LiveData<List<OneShotTask>>

    @Query("SELECT * FROM one_shot_home_task WHERE done = 1 ORDER BY dayExceeded DESC")
    fun getAllDoneTask() : LiveData<List<OneShotTask>>

    @Query("SELECT * FROM one_shot_home_task WHERE done = 0 AND oneCategory = :category ORDER BY dayExceeded DESC")
    fun getAllTaskToDoByCategory(category : String) : LiveData<List<OneShotTask>>

    @Query("SELECT * FROM one_shot_home_task WHERE done = 1 AND oneCategory = :category ORDER BY dayExceeded DESC")
    fun getAllDoneTaskByCategory(category : String) : LiveData<List<OneShotTask>>

    @Query("SELECT * FROM one_shot_home_task")
    fun getAllTaskAsync() : List<OneShotTask>

    @Query("SELECT * FROM one_shot_home_task WHERE id = :id")
    fun getOneShotTaskById(id : Long) : OneShotTask?


}
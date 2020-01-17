package com.synowkrz.housemanager.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.synowkrz.housemanager.homeTaskList.model.DoneTask

@Dao
interface DoneTaskDao {

    @Insert
    fun insert(doneTask : DoneTask)

    @Delete
    fun delete(doneTask: DoneTask)

    @Query("SELECT * FROM done_task WHERE name = :name ORDER BY id DESC")
    fun getAllDoneTaskByName(name : String) : LiveData<List<DoneTask>>

    @Query("SELECT * FROM done_task WHERE id = :id")
    fun getDoneTaskById(id : Long) :  DoneTask?

    @Query("SELECT * FROM done_task")
    fun getAllDoneTaskAsync() : List<DoneTask>
}
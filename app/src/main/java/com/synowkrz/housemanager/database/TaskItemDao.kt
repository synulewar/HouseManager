package com.synowkrz.housemanager.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.synowkrz.housemanager.model.TaskGridItem


@Dao
interface TaskItemDao {
    @Insert
    fun insert(taskGridItem: TaskGridItem)

    @Update
    fun update(taskGridItem: TaskGridItem)

    @Query("DELETE FROM task_grid_table WHERE name = :name")
    fun deleteTask(name: String)

    @Query("SELECT * FROM task_grid_table")
    fun getAllTasks(): LiveData<List<TaskGridItem>>
}
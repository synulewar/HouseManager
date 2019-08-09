package com.synowkrz.housemanager.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.synowkrz.housemanager.babyTask.model.Feeding

@Dao
interface FeedingDao {
    @Insert
    fun insert(feeding: Feeding)

    @Query("SELECT * FROM feeding_table")
    fun getAllFeedings() : LiveData<List<Feeding>>

    @Query("SELECT * FROM feeding_table WHERE profielName = :name")
    fun getAllFeedingsByName(name: String) : LiveData<List<Feeding>>
}
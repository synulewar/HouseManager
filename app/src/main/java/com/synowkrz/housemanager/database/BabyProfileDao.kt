package com.synowkrz.housemanager.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.synowkrz.housemanager.babyTask.model.BabyProfile

@Dao
interface BabyProfileDao {
    @Insert
    fun insert(babyProfile: BabyProfile)

    @Update
    fun update(babyProfile: BabyProfile)

    @Query("DELETE FROM baby_profile WHERE name = :name")
    fun deleteBaby(name: String)

    @Query("SELECT * FROM baby_profile")
    fun getAllBabies(): LiveData<List<BabyProfile>>

    @Query("SELECT * FROM baby_profile")
    fun getAllBabiesData(): List<BabyProfile>

    @Query("SELECT * FROM baby_profile WHERE name = :name")
    fun getBabyByName(name: String) : BabyProfile?
}
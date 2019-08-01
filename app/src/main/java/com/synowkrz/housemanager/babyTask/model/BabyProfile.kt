package com.synowkrz.housemanager.babyTask.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "baby_profile")
class BabyProfile(@PrimaryKey
                  val name: String,
                  val photo: String,
                  val birthDate: String) {
}
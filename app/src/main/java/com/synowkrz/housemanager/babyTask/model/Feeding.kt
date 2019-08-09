package com.synowkrz.housemanager.babyTask.model

import androidx.room.Entity
import androidx.room.PrimaryKey


enum class FeedingType {
    LEFT, RIGHT, BOTTLE, SOLID
}

@Entity(tableName = "feeding_table")
class Feeding(@PrimaryKey
              var startTime : Long,
              val profielName: String,
              val feedingType: FeedingType,
              var endTime : Long = 0,
              var duration: Long = 0,
              var amount: Int = 0)
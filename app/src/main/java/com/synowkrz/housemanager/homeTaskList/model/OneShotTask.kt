package com.synowkrz.housemanager.homeTaskList.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Entity(tableName = "one_shot_home_task")
data class OneShotTask(val name : String = "",
                       var dueDate : String = "",
                       var oneCategory: OneCategory = OneCategory.OTHER,
                       var dayExceeded : Int = 0,
                       var done : Int = 0,
                       @PrimaryKey val id : Long = System.currentTimeMillis()) {

    constructor(name : String, dueDate: String, oneCategory: OneCategory) : this(name, dueDate, oneCategory, 0) {
        refreshTask()
    }

    fun refreshTask() {
        dayExceeded = ChronoUnit.DAYS.between(LocalDate.parse(dueDate), LocalDate.now()).toInt()
    }

    fun doTask() {
        done = 1
    }

    fun unDoTask() {
        done = 0
    }
}

enum class OneCategory {
    KIDS, HOME, NEW_HOME, CAR, SHOP, PHONE, OTHER
}
package com.synowkrz.housemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TaskTypes {
    BABY, CALENDAR, SHOP_LIST, TASK_LIST, CUSTOM
}

@Entity(tableName = "task_grid_table")
class TaskGridItem(
    @PrimaryKey
    val name: String,
    val iconResource: String,
    val taskType: TaskTypes
)


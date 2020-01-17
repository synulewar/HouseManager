package com.synowkrz.housemanager.homeTaskList.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "done_task")
class DoneTask(var name : String = "",
               var date : String = "",
               @PrimaryKey
               var id : Long = System.currentTimeMillis()) {
}
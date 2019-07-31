package com.synowkrz.housemanager

import com.synowkrz.housemanager.model.TaskTypes

fun convertTypeTaskToResourceString(taskTypes: TaskTypes) : String {
    return when(taskTypes) {
        TaskTypes.BABY -> "kid"
        TaskTypes.CALENDAR -> "calendar"
        TaskTypes.SHOP_LIST -> "shoplist"
        TaskTypes.TASK_LIST -> "taskslist"
        TaskTypes.CUSTOM -> "custom"
    }
}
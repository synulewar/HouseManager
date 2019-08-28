package com.synowkrz.housemanager

import com.synowkrz.housemanager.model.TaskTypes
import com.synowkrz.housemanager.shopList.model.Measurements

fun convertTypeTaskToResourceString(taskTypes: TaskTypes) : String {
    return when(taskTypes) {
        TaskTypes.BABY -> "kid"
        TaskTypes.CALENDAR -> "calendar"
        TaskTypes.SHOP_LIST -> "shoplist"
        TaskTypes.TASK_LIST -> "taskslist"
        TaskTypes.CUSTOM -> "custom"
    }
}

fun getAmountFormat(measurements: Measurements) : String {
    return when(measurements) {
        Measurements.WEIGHT,
        Measurements.VOLUME -> "%.1f"
        Measurements.QUANTITY -> "%.0f"
    }
}

fun getMeasurmentString(measurements: Measurements) : String {
    return when(measurements) {
        Measurements.WEIGHT -> "kg"
        Measurements.VOLUME -> "l"
        Measurements.QUANTITY -> ""
    }
}

fun getCategoryFromString(categoryString: String) {

}
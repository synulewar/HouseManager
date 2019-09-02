package com.synowkrz.housemanager

import android.graphics.Color
import com.synowkrz.housemanager.model.TaskTypes
import com.synowkrz.housemanager.shopList.model.Category
import com.synowkrz.housemanager.shopList.model.Measurements

val TAG = "KRZYSIO"
val DELIMITER = ","

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

fun getColorByCategory(category: Category) : Int {
    return when (category) {
        Category.BREAD -> Color.YELLOW
        Category.FRUIT_VEGETABLES -> Color.GREEN
        Category.MEAT -> Color.RED
        Category.DAIRY -> Color.WHITE
        Category.ALCOHOL -> Color.GRAY
        Category.SWEETS -> Color.CYAN
        Category.SNACKS -> Color.DKGRAY
        Category.HYGIENE -> Color.MAGENTA
        Category.DRINKABLES -> Color.LTGRAY
        Category.OTHER -> Color.BLUE
        else -> Color.WHITE
    }
}

fun getCategoryFromString(categoryString: String) {

}
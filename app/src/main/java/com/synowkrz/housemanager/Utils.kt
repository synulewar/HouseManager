package com.synowkrz.housemanager

import android.app.Application
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.synowkrz.housemanager.model.TaskTypes
import com.synowkrz.housemanager.shopList.model.Category
import com.synowkrz.housemanager.shopList.model.Measurements

val TAG = "KRZYSIO"
val DELIMITER = ","
val SHOP_LIST_KEY = "shopLists"
val PERSISTENT_SHOP_KEY = "persistentShopItem"
val SHOP_AREA_KEY = "shopArea"
val SHOP_ITEM_KEY = "shopItem"

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

fun getColorByCategory(category: Category, app: Application) : Int {
    return when (category) {
        Category.BREAD -> ContextCompat.getColor(app, R.color.yellow)
        Category.FRUIT_VEGETABLES -> ContextCompat.getColor(app, R.color.green)
        Category.MEAT -> ContextCompat.getColor(app, R.color.red)
        Category.DAIRY -> ContextCompat.getColor(app, R.color.white)
        Category.ALCOHOL -> ContextCompat.getColor(app, R.color.blue)
        Category.SWEETS -> ContextCompat.getColor(app, R.color.grey)
        Category.SNACKS -> ContextCompat.getColor(app, R.color.dark_grey)
        Category.HYGIENE -> ContextCompat.getColor(app, R.color.pink)
        Category.DRINKABLES -> ContextCompat.getColor(app, R.color.cyan)
        Category.OTHER -> ContextCompat.getColor(app, R.color.violet)
        Category.CAN_AND_PRESERVES -> ContextCompat.getColor(app, R.color.orange)
        else -> Color.WHITE
    }
}
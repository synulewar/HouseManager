package com.synowkrz.housemanager.shopList.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persistent_shop_item")
class PersistentShopItem(@PrimaryKey
                         var name: String = "",
                         var category: Category = Category.ALL,
                         var defaultAmount: Double = 0.0,
                         var measurements: Measurements = Measurements.QUANTITY,
                         var usage : Long = 0)


@Entity(tableName = "shop_item")
data class ShopItem(var name: String = "",
                    var category: Category = Category.ALL,
                    var amount: Double = 0.0,
                    var measurements: Measurements = Measurements.QUANTITY,
                    var listName: String = "",
                    var active: Boolean = true,
                    @PrimaryKey
                    var id : Long = System.currentTimeMillis()) {
}

enum class Measurements {
    WEIGHT, VOLUME, QUANTITY
}

enum class Category {
    BREAD, FRUIT_VEGETABLES, MEAT, DAIRY, CAN_AND_PRESERVES, ALCOHOL, SWEETS, SNACKS, HYGIENE, DRINKABLES, OTHER, ALL
}


package com.synowkrz.housemanager.shopList.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persistent_shop_item")
class PersistentShopItem(@PrimaryKey
                         val name: String,
                         val category: Category,
                         var defaultAmount: Double,
                         var measurements: Measurements,
                         var usage : Long)


@Entity(tableName = "shop_item")
data class ShopItem(@PrimaryKey(autoGenerate = true)
                    val id : Int = 0,
                    val name: String,
                    val category: Category,
                    var amount: Double,
                    var measurements: Measurements,
                    var listName: String,
                    var active: Boolean = true) {
}

enum class Measurements {
    WEIGHT, VOLUME, QUANTITY
}

enum class Category {
    BREAD, FRUIT_VEGETABLES, MEAT, DAIRY, ALCOHOL, SWEETS, SNACKS, HYGIENE, DRINKABLES, OTHER
}


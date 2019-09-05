package com.synowkrz.housemanager.shopList.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_area")
class ShopArea(@PrimaryKey var name: String ="", var areas : String = "") {
}
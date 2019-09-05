package com.synowkrz.housemanager.shopList.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_list")
class ShopList(@PrimaryKey
               var name: String = "",
               var shopName: String = "")
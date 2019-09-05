package com.synowkrz.housemanager.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.synowkrz.housemanager.shopList.model.ShopItem

@Dao
interface ShopItemDao {
    @Insert
    fun insert(shopItem: ShopItem)

    @Update
    fun update(shopItem: ShopItem)

    @Query("SELECT * FROM shop_item where listName = :listName and active = 1")
    fun getAllActivetemsFromList(listName : String) : LiveData<List<ShopItem>>

    @Query("SELECT * FROM shop_item where listName = :listName and active = 0")
    fun getAllInactivetemsFromList(listName : String) : LiveData<List<ShopItem>>

    @Query("SELECT * FROM shop_item")
    fun getAllItemsAsync() : List<ShopItem>

    @Delete
    fun deleteShopItem(shopItem: ShopItem)
}
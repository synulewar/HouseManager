package com.synowkrz.housemanager.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.synowkrz.housemanager.shopList.model.PersistentShopItem

@Dao
interface PersistentShopItemDao {
    @Insert
    fun insert(persistentShopItem: PersistentShopItem)

    @Update
    fun update(persistentShopItem: PersistentShopItem)

    @Query("SELECT * FROM persistent_shop_item ORDER BY usage DESC")
    fun getAllPersistenShopItem() : LiveData<List<PersistentShopItem>>

    @Query("SELECT * FROM persistent_shop_item WHERE name = :name")
    fun getPersistentShopItemByName(name: String) : PersistentShopItem?

    @Query("SELECT * FROM persistent_shop_item WHERE category = :category")
    fun getPersistentShopItemByCategory(category: String) : LiveData<List<PersistentShopItem>>
}
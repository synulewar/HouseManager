package com.synowkrz.housemanager.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.synowkrz.housemanager.shopList.model.ShopArea

@Dao
interface ShopAreaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(shopArea: ShopArea)

    @Update
    fun update(shopArea: ShopArea)

    @Query("DELETE FROM shop_area WHERE name = :name")
    fun deleteArea(name: String)

    @Query("SELECT * FROM shop_area")
    fun getAllShopAreas() : LiveData<List<ShopArea>>

    @Query("SELECT * FROM shop_area")
    fun getAllShopAreasAsync() : List<ShopArea>

    @Query("SELECT * FROM shop_area WHERE name = :name")
    fun getShopAreByName(name: String) : ShopArea
}
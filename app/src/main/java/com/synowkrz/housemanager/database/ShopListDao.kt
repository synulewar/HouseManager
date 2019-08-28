package com.synowkrz.housemanager.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.synowkrz.housemanager.shopList.model.ShopList

@Dao
interface ShopListDao {
    @Insert
    fun insert(shopList: ShopList)

    @Update
    fun update(shopList: ShopList)

    @Query("SELECT * FROM shop_list")
    fun getAllShopList() : LiveData<List<ShopList>>

    @Query("SELECT * FROM shop_list where name = :name")
    fun getShopList(name: String) : LiveData<ShopList>

}
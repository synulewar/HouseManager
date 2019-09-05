package com.synowkrz.housemanager.shopList.shopArea

import android.app.Application
import androidx.lifecycle.*
import com.synowkrz.housemanager.repository.HouseRepository
import com.synowkrz.housemanager.shopList.model.ShopArea
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ShopAreaListViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = HouseRepository(app)
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _itemList = repository.getAllShopAreas()

    val itemList : LiveData<List<ShopArea>>
        get() = _itemList

    private var _newShopArea = MutableLiveData<Boolean>()
    val newShopArea : LiveData<Boolean>
        get() = _newShopArea


    fun onAddNewShopArea() {
        _newShopArea.value = true
    }

    fun onAddNewShopAreaFinished() {
        _newShopArea.value = false
    }

    fun insertNewShopArea(name: String) {
        val shopArea = ShopArea(name, "")
        viewModelScope.launch {
            repository.insertNewShopArea(shopArea)
        }
    }

    fun deleteShopArea(shopArea: ShopArea) {
        viewModelScope.launch {
            repository.deletetShopArea(shopArea)
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ShopAreaListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ShopAreaListViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

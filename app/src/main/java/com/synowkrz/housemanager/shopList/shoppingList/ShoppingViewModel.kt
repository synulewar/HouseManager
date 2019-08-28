package com.synowkrz.housemanager.shopList.shoppingList

import android.app.Application
import androidx.lifecycle.*
import com.synowkrz.housemanager.repository.HouseRepository
import com.synowkrz.housemanager.shopList.model.ShopItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ShoppingViewModel(val app: Application, val listName: String) : AndroidViewModel(app) {

    private val repository = HouseRepository(app)
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private var _itemList = repository.getAllActiveItems(listName)


    private var _onAddProductPressed = MutableLiveData<Boolean>()
    val onAddProductPressed : LiveData<Boolean>
        get() = _onAddProductPressed

    val itemList : LiveData<List<ShopItem>>
        get() = _itemList


    fun onAddProduct() {
        _onAddProductPressed.value = true
    }

    fun onAddProductFinished() {
        _onAddProductPressed.value = false
    }

    fun onItemBought(shopItem: ShopItem) {
        shopItem.active = false
        viewModelScope.launch {
            repository.updateShopItem(shopItem)
        }
    }

    class Factory(val app: Application, val listName: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ShoppingViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ShoppingViewModel(app, listName) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

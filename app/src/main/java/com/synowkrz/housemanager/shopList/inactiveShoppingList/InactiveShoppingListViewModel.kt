package com.synowkrz.housemanager.shopList.inactiveShoppingList

import android.app.Application
import androidx.lifecycle.*
import com.synowkrz.housemanager.repository.HouseRepository
import com.synowkrz.housemanager.shopList.model.ShopItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class InactiveShoppingListViewModel(app: Application, val listName: String) : AndroidViewModel(app) {


    private val repository = HouseRepository(app)
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private var _itemList = repository.getAllInactiveItems(listName)

    val itemList : LiveData<List<ShopItem>>
        get() = _itemList

    private var _activItem = MutableLiveData<Boolean>()
    val activItem : LiveData<Boolean>
        get() = _activItem



    fun onActivated(shopItem: ShopItem) {
        shopItem.active = true
        viewModelScope.launch {
            repository.updateShopItem(shopItem)
        }
    }

    fun goToActiveItems() {
        _activItem.value = true
    }

    fun onactiveItemFinished() {
        _activItem.value = false
    }


    class Factory(val app: Application, val listName: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(InactiveShoppingListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return InactiveShoppingListViewModel(app, listName) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

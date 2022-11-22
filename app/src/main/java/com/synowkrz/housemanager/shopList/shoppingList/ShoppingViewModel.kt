package com.synowkrz.housemanager.shopList.shoppingList

import android.app.Application
import androidx.lifecycle.*
import com.synowkrz.housemanager.DELIMITER
import com.synowkrz.housemanager.repository.HouseRepository
import com.synowkrz.housemanager.shopList.model.Category
import com.synowkrz.housemanager.shopList.model.ShopItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ShoppingViewModel(val app: Application, val listName: String, val sortString: String) : AndroidViewModel(app) {

    private val repository = HouseRepository(app)
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val sortedCategoryList = let {
        val sortCat = mutableListOf<Category>()
        if (sortString != "") {
            for (catString in sortString.split(DELIMITER)) {
                sortCat.add(Category.valueOf(catString))
            }
        }
        sortCat
    }

    private val unsortedItems = repository.getAllActiveItems(listName)

    var itemSorter : (List<ShopItem>) -> List<ShopItem> = {
        var sortedList = mutableListOf<ShopItem>()
        for (cat in sortedCategoryList) {
            for (item in it) {
                if (item.category == cat) {
                    sortedList.add(item)
                }
            }
        }
        for (item in it) {
            if (!sortedList.contains(item)) {
                sortedList.add(item)
            }
        }
        sortedList
    }

    private var _itemList = Transformations.map(unsortedItems, itemSorter)


    private var _onAddProductPressed = MutableLiveData<Boolean>()
    val onAddProductPressed : LiveData<Boolean>
        get() = _onAddProductPressed


    private var _onGoToInactive = MutableLiveData<Boolean>()
    val onGoToInactive : LiveData<Boolean>
        get() = _onGoToInactive

    val itemList : LiveData<List<ShopItem>>
        get() = _itemList


    fun onAddProduct() {
        _onAddProductPressed.value = true
    }

    fun onInactive() {
        _onGoToInactive.value = true
    }

    fun onGotoInactiveFinished() {
        _onGoToInactive.value = false
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

    class Factory(val app: Application, val listName: String, val sortString: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ShoppingViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ShoppingViewModel(app, listName, sortString) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

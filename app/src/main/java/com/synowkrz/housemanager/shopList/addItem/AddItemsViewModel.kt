package com.synowkrz.housemanager.shopList.addItem

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.synowkrz.housemanager.TAG
import com.synowkrz.housemanager.repository.HouseRepository
import com.synowkrz.housemanager.shopList.model.Category
import com.synowkrz.housemanager.shopList.model.Measurements
import com.synowkrz.housemanager.shopList.model.PersistentShopItem
import com.synowkrz.housemanager.shopList.model.ShopItem
import kotlinx.coroutines.*

class AddItemsViewModel(val app: Application, val listName: String) : AndroidViewModel(app) {

    private val repository = HouseRepository(app)
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private var currentCategory = Category.ALL
    private var _itemList = MutableLiveData<List<PersistentShopItem>>()
    val itemList : LiveData<List<PersistentShopItem>>
        get() = _itemList

    private val _onNewItemAdded = MutableLiveData<Boolean>()
    val onNewItemAdded : LiveData<Boolean>
        get() = _onNewItemAdded


    init {

    }

    fun onProductAdd(persistentShopItem: PersistentShopItem, amount: String) {
        persistentShopItem.usage += 1
        viewModelScope.launch {
            var amountValue = amount.replace(",", ".").toDouble()
            repository.insertShopItem(ShopItem(persistentShopItem.name, persistentShopItem.category, amountValue, persistentShopItem.measurements, listName))
            repository.updatePersistentShopItem(persistentShopItem)
        }
    }

    fun onAddNewItem() {
        _onNewItemAdded.value = true
    }

    fun onAddNewItemFinished() {
        _onNewItemAdded.value = false
    }

    fun addNewPersistentShopItem(name: String, amount: String, category: Category, measurment: Measurements) {
        Log.d(TAG, " ${name}, ${amount}, ${category}, ${measurment}")
        var newPersistentItem = PersistentShopItem(name, category, amount.toDouble(), measurment, 1)
        viewModelScope.launch {
            repository.insertNewPersistentShopItem(newPersistentItem)
            changeDataSource(currentCategory)
        }
    }

    fun changeDataSource(category: Category) {
        Log.d(TAG, "Choosen category ${category}")
        currentCategory = category
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (category == Category.ALL) {
                    _itemList.postValue(repository.getAllPersistentItemAsync())
                } else {
                    _itemList.postValue(repository.getAllPersistentItemByCategoryAsync(category.toString()))
                }
            }
        }
    }

    fun changeDataSource(query : String) {
        Log.d(TAG, "Change data source query ${query}")


        if (query.isEmpty()) {
            changeDataSource(currentCategory)
        }

        if (query.length < 2) {
            return
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (currentCategory == Category.ALL) {
                    _itemList.postValue(repository.getPersistentShopItemByNamePart(query))
                } else {
                    _itemList.postValue(repository.getPersistentShopItemByNamePartAndCategory(query, currentCategory))
                }
            }
        }

    }

    class Factory(val app: Application, val listName: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddItemsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AddItemsViewModel(app, listName ) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}

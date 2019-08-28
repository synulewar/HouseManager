package com.synowkrz.housemanager.shopList.addItem

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.synowkrz.housemanager.repository.HouseRepository
import com.synowkrz.housemanager.shopList.model.Category
import com.synowkrz.housemanager.shopList.model.Measurements
import com.synowkrz.housemanager.shopList.model.PersistentShopItem
import com.synowkrz.housemanager.shopList.model.ShopItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AddItemsViewModel(val app: Application, val listName: String) : AndroidViewModel(app) {

    private val repository = HouseRepository(app)
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    var itemList : LiveData<List<PersistentShopItem>>


    private val _onNewItemAdded = MutableLiveData<Boolean>()
    val onNewItemAdded : LiveData<Boolean>
        get() = _onNewItemAdded



    init {
        itemList = repository.persistentItems

    }

    fun onProductAdd(persistentShopItem: PersistentShopItem, amount: String) {
        persistentShopItem.usage += 1
        viewModelScope.launch {
            var amountValue = amount.replace(",", ".").toDouble()
            repository.insertShopItem(ShopItem(0,persistentShopItem.name, persistentShopItem.category, amountValue, persistentShopItem.measurements, listName))
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
        Log.d("KRZYSIO", " ${name}, ${amount}, ${category}, ${measurment}")
        var newPersistentItem = PersistentShopItem(name, category, amount.toDouble(), measurment, 1)
        viewModelScope.launch {
            repository.insertNewPersistentShopItem(newPersistentItem)
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

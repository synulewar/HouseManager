package com.synowkrz.housemanager.shopList.itemList

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.synowkrz.housemanager.repository.HouseRepository
import com.synowkrz.housemanager.shopList.model.Category
import com.synowkrz.housemanager.shopList.model.Measurements
import com.synowkrz.housemanager.shopList.model.PersistentShopItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ItemListViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = HouseRepository(app)
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private var _itemList = repository.getAllPersistentItems()
    val itemList : LiveData<List<PersistentShopItem>>
        get() = _itemList

    private var _onAddPersistentItem = MutableLiveData<Boolean>()
    val onAddPersistentItem : LiveData<Boolean>
        get() = _onAddPersistentItem



    fun addNewPersistentShopItem(name: String, amount: String, category: Category, measurment: Measurements) {
        Log.d("KRZYSIO", " ${name}, ${amount}, ${category}, ${measurment}")
        var newPersistentItem = PersistentShopItem(name, category, amount.toDouble(), measurment, 1)
        viewModelScope.launch {
            repository.insertNewPersistentShopItem(newPersistentItem)
        }
    }


    fun onAddNewItem() {
        _onAddPersistentItem.value = true
    }


    fun onAddNewItemFinished() {
        _onAddPersistentItem.value = false
    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ItemListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ItemListViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

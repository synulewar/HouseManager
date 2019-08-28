package com.synowkrz.housemanager.shopList.shopList

import android.app.Application
import androidx.lifecycle.*
import com.synowkrz.housemanager.repository.HouseRepository
import com.synowkrz.housemanager.shopList.model.ShopList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ShopListViewModel(val app: Application) : AndroidViewModel(app) {

    private val repository = HouseRepository(app)
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    private var _onAddNewList = MutableLiveData<Boolean>()
    val onAddNewList : LiveData<Boolean>
        get() = _onAddNewList

    val itemList : LiveData<List<ShopList>>

    init {
        initializeShopItems()
        itemList = repository.shopList
    }


    fun onAddListPressed() {
        _onAddNewList.value = true
    }

    fun onAddNewListFinished() {
        _onAddNewList.value = false
    }

    fun insertNewShopList(shopList: ShopList) {
        viewModelScope.launch {
            repository.insertNewShopList(shopList)
        }
    }

    private fun initializeShopItems() {

    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ShopListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ShopListViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

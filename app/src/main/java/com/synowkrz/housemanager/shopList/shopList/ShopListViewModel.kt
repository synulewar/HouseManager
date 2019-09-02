package com.synowkrz.housemanager.shopList.shopList

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.synowkrz.housemanager.TAG
import com.synowkrz.housemanager.repository.HouseRepository
import com.synowkrz.housemanager.shopList.model.ShopArea
import com.synowkrz.housemanager.shopList.model.ShopList
import kotlinx.coroutines.*

class ShopListViewModel(val app: Application) : AndroidViewModel(app) {

    private val repository = HouseRepository(app)
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val shopAreas : LiveData<List<ShopArea>> = repository.shopAreas
    private var asyncShopArea : List<ShopArea>? = null


    var areasConverter : (List<ShopArea>) -> List<String> = {
        val stringLsit = mutableListOf<String>()
        for (area in it) {
            stringLsit.add(area.name)
        }
        stringLsit
    }

    val areaNames : LiveData<List<String>> = Transformations.map(shopAreas, areasConverter )


    private var _onAddNewList = MutableLiveData<Boolean>()
    val onAddNewList : LiveData<Boolean>
        get() = _onAddNewList

    val itemList : LiveData<List<ShopList>>

    init {
        initializeShopItems()
        itemList = repository.shopList
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                asyncShopArea = repository.getAllShopAreaAsync()
                Log.d(TAG, "Async shop are size available")
            }
        }
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

    fun getSortString(name: String) : String {
        if (asyncShopArea == null) {
            return ""
        }

        for (area in asyncShopArea!!) {
            if (area.name == name) {
                return area.areas
            }
        }
        return ""
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

package com.synowkrz.housemanager.shopList.shopAreaConfigurator

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.synowkrz.housemanager.DELIMITER
import com.synowkrz.housemanager.TAG
import com.synowkrz.housemanager.repository.HouseRepository
import com.synowkrz.housemanager.shopList.model.Category
import com.synowkrz.housemanager.shopList.model.ShopArea
import kotlinx.coroutines.*

class ShopAreaConfiguratorViewModel(val app: Application, val shopAreaName: String) : AndroidViewModel(app) {

    private val repository = HouseRepository(app)
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var areaNotAssign = mutableListOf<Category>()
    private var areaAssign = mutableListOf<Category>()

    private var _areasList = MutableLiveData<List<Category>>()
    private var _shopPath = MutableLiveData<List<Category>>()

    val areasList : LiveData<List<Category>>
        get() = _areasList

    val shopPath : LiveData<List<Category>>
        get() = _shopPath

    lateinit var currentShopArea: ShopArea

    init {
        Log.d(TAG, "ShopAreaConfiguratorViewModel init")
        prepareCategory()
    }


    fun prepareCategory() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                areaNotAssign.addAll(Category.values())
                areaNotAssign.remove(Category.ALL)
                currentShopArea = repository.getShopAreaByName(shopAreaName)!!
                Log.d(TAG, "Saved areas ${currentShopArea.areas}")
                for (cat in currentShopArea.areas.split(DELIMITER)) {
                    if (cat == "") {
                        continue
                    }
                    var category = Category.valueOf(cat)
                    areaAssign.add(category)
                    areaNotAssign.remove(category)
                }
                _areasList.postValue(areaNotAssign)
                _shopPath.postValue(areaAssign)
            }
        }
    }

    fun switchFromAreasToPath(category: Category) {
        areaNotAssign.remove(category)
        areaAssign.add(category)
        refreshData()
    }

    fun switchFromPathToAreas(category: Category) {
        areaNotAssign.add(category)
        areaAssign.remove(category)
        refreshData()
    }

    fun refreshData() {
        Log.d(TAG, "notAssign ${areaNotAssign.size} assign ${areaAssign.size}")

        _areasList.value = areaNotAssign
        _shopPath.value = areaAssign
        currentShopArea.areas = convertListToString(areaAssign)
        viewModelScope.launch {
            repository.updateShopArea(currentShopArea)
        }
    }


    fun convertListToString(categoryList : List<Category>) : String {
        var sb = StringBuilder()
        for (category in categoryList) {
            if (!sb.isEmpty()) {
                sb.append(DELIMITER)
            }
            sb.append(category.toString())
        }
        return sb.toString()
    }

    class Factory(val app: Application, val shopAreaName: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ShopAreaConfiguratorViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ShopAreaConfiguratorViewModel(app, shopAreaName) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

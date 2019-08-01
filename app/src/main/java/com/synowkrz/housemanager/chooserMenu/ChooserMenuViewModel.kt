package com.synowkrz.housemanager.chooserMenu

import android.app.Application
import androidx.lifecycle.*
import com.synowkrz.housemanager.convertTypeTaskToResourceString
import com.synowkrz.housemanager.model.TaskGridItem
import com.synowkrz.housemanager.model.TaskTypes

class ChooserMenuViewModel(app: Application): AndroidViewModel(app) {

    private val _itemList = MutableLiveData<List<TaskGridItem>>()

    val itemList : LiveData<List<TaskGridItem>>
        get() = _itemList


    private val _itemAdded = MutableLiveData<TaskTypes>()
    val itemAdded : LiveData<TaskTypes>
        get() = _itemAdded

    init {
        var e1 = TaskGridItem("Kid", convertTypeTaskToResourceString(TaskTypes.BABY), TaskTypes.BABY)
        var e2 = TaskGridItem("HouseWork", convertTypeTaskToResourceString(TaskTypes.TASK_LIST), TaskTypes.TASK_LIST)
        var e3 = TaskGridItem("Calendar", convertTypeTaskToResourceString(TaskTypes.CALENDAR), TaskTypes.CALENDAR)
        var e4 = TaskGridItem("ShopList", convertTypeTaskToResourceString(TaskTypes.SHOP_LIST), TaskTypes.SHOP_LIST)
        var e5 = TaskGridItem("Custom", convertTypeTaskToResourceString(TaskTypes.CUSTOM), TaskTypes.CUSTOM)
        _itemList.value = mutableListOf(e1, e2, e3, e4, e5)
    }

    fun onTaskChoosen(taskGridItem: TaskGridItem) {
        _itemAdded.value = taskGridItem.taskType
    }

    fun onTaskChoosenCompleted() {
        _itemAdded.value = null
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ChooserMenuViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ChooserMenuViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
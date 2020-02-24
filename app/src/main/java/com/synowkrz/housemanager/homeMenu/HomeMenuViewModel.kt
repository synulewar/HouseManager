package com.synowkrz.housemanager.homeMenu

import android.app.Application
import androidx.lifecycle.*
import com.synowkrz.housemanager.model.TaskGridItem
import com.synowkrz.housemanager.repository.HouseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeMenuViewModel @Inject constructor(val repository: HouseRepository, val app : Application) : AndroidViewModel(app) {

    private val _itemList = MutableLiveData<List<TaskGridItem>>()

    val itemList : LiveData<List<TaskGridItem>>
    //get() = _itemList

    private val _newTaskPressed = MutableLiveData<Boolean>()
    val newTaskPressed : LiveData<Boolean>
    get() = _newTaskPressed

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)



    init {
       itemList = repository.items
    }

    fun onAddNewTaskPressed() {
        _newTaskPressed.value = true
    }

    fun onAddNewTaskCompleted() {
        _newTaskPressed.value = false
    }

    fun onItemLongPress(taskGridItem: TaskGridItem) {
        viewModelScope.launch {
            repository.removeTask(taskGridItem)
        }
    }
}

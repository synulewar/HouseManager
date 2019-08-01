package com.synowkrz.housemanager.homeMenu

import android.app.Application
import androidx.lifecycle.*
import com.synowkrz.housemanager.model.TaskGridItem
import com.synowkrz.housemanager.repository.HouseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeMenuViewModel(app: Application) : AndroidViewModel(app) {

    private val _itemList = MutableLiveData<List<TaskGridItem>>()

    val itemList : LiveData<List<TaskGridItem>>
    //get() = _itemList

    private val _newTaskPressed = MutableLiveData<Boolean>()
    val newTaskPressed : LiveData<Boolean>
    get() = _newTaskPressed

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val repository = HouseRepository(app)


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


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeMenuViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeMenuViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

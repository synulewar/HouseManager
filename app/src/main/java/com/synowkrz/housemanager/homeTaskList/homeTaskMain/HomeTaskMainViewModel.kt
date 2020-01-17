package com.synowkrz.housemanager.homeTaskList.homeTaskMain

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.synowkrz.housemanager.TAG
import com.synowkrz.housemanager.homeTaskList.model.HomeTask
import com.synowkrz.housemanager.homeTaskList.model.Interval
import com.synowkrz.housemanager.repository.HouseRepository
import kotlinx.coroutines.*
import java.lang.Exception
import java.time.LocalDate

class HomeTaskMainViewModel(val app: Application) : AndroidViewModel(app) {

    private var _onAddTaskPressed = MutableLiveData<Boolean>()
    private val repository = HouseRepository(app)
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val onAddTaskPressed : LiveData<Boolean>
        get() = _onAddTaskPressed


    private var _itemList = MutableLiveData<List<HomeTask>>()
    val itemList = repository.tasks



    init {
        refreshTaskList()
    }


    fun onAddTaskPressed() {
        _onAddTaskPressed.value = true
    }

    fun onAddTaskPressedFinished() {
        _onAddTaskPressed.value = false
    }


    fun insertNewHomeTask(name : String, frequency : Int, interval : Interval) {

        var dueDate = HomeTask.calculateDueDate(LocalDate.now(), interval, frequency)
        var expired = HomeTask.isTaskExpired(dueDate)
        var daysExceeded = HomeTask.calculateDaysExceeded(dueDate)
        try {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    Log.d(TAG, "Insert new HomeTask dueDate: ${dueDate} expired : ${expired} calculateDaysExceeded ${daysExceeded}")
                    repository.insertNewHomeTask(HomeTask(name, frequency, interval, dueDate.toString(), daysExceeded, expired))
                }
            }
        } catch (e : Exception) {
            Log.e(TAG, e.message ?: "empty message")
        }
    }

    fun refreshTaskList() {
        viewModelScope.launch {
            repository.refreshTaskList()
        }
    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeTaskMainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeTaskMainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

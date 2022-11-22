package com.synowkrz.housemanager.homeTaskList.homeTaskDetail

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.synowkrz.housemanager.TAG
import com.synowkrz.housemanager.homeTaskList.model.DoneTask
import com.synowkrz.housemanager.homeTaskList.model.HomeTask
import com.synowkrz.housemanager.homeTaskList.model.Interval
import com.synowkrz.housemanager.repository.HouseRepository
import kotlinx.coroutines.*
import java.time.LocalDate

class HomeTaskDetailViewModel(val app : Application, val name : String) : AndroidViewModel(app) {


    private val repository = HouseRepository(app)
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    val itemList = repository.getAllDoneTaskByName(name)

    private var _editHomeTask = MutableLiveData<HomeTask>()
    private var _deleteHomeTask = MutableLiveData<HomeTask>()


    val editHomeTask : LiveData<HomeTask>
        get() = _editHomeTask


    val deleteHomeTask : LiveData<HomeTask>
        get() = _deleteHomeTask


    fun addNewDoneTask() {
        Log.d(TAG, "Add new taskDone $name")
        viewModelScope.launch {
            repository.insertNewDoneTask(DoneTask(name, LocalDate.now().toString()))
            withContext(Dispatchers.IO) {
                var homeTask = repository.getHomeTaskByName(name)
                homeTask?.let {
                    it.doTask()
                    repository.updateHomeTask(it)
                }
            }
        }
    }

    fun onDeleteHomeTask() {
        Log.d(TAG, "onDeleteHomeTask")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var homeTask = repository.getHomeTaskByName(name)
                Log.d(TAG, "onDeleteHomeTask got ${homeTask.toString()} from db")
                homeTask?.let {
                    _deleteHomeTask.postValue(homeTask)
                }
            }
        }

    }

    fun onEditHomeTask() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var homeTask = repository.getHomeTaskByName(name)
                homeTask?.let {
                    _editHomeTask.postValue(homeTask)
                }
            }
        }
    }

    fun removeHomeTask(homeTask: HomeTask) {
        viewModelScope.launch {
            repository.deleteHomeTask(homeTask)
        }
    }

    fun onEditFinished() {
        _editHomeTask.value = null
    }


    fun onDeleteFinished() {
        _deleteHomeTask.value = null
    }

    fun updateHomeTask(homeTask: HomeTask, frequency : Int, interval : Interval) {
        Log.d(TAG, "updateHomeTask frequency $frequency interval $interval")
        homeTask.recalculateTask(frequency, interval)
        viewModelScope.launch {
            repository.updateHomeTask(homeTask)
        }
    }

    class Factory(val app: Application, val name : String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeTaskDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeTaskDetailViewModel(app, name) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

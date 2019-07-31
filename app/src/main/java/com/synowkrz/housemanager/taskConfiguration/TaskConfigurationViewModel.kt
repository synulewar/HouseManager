package com.synowkrz.housemanager.taskConfiguration

import android.app.Application
import androidx.lifecycle.*
import com.synowkrz.housemanager.convertTypeTaskToResourceString
import com.synowkrz.housemanager.model.TaskGridItem
import com.synowkrz.housemanager.model.TaskTypes
import com.synowkrz.housemanager.repository.HouseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TaskConfigurationViewModel(app: Application, val taskType: TaskTypes) : AndroidViewModel(app) {

    private val repository = HouseRepository(app)
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _imageResource = MutableLiveData<String>()
    val imageResource : LiveData<String>
    get() = _imageResource


    private val _emptyString = MutableLiveData<Boolean>()
    val emptyString : LiveData<Boolean>
        get() = _emptyString

    private val _databaseError = MutableLiveData<Boolean>()
    val databaseError : LiveData<Boolean>
        get() = _databaseError


    private val _itemAdded = MutableLiveData<Boolean>()
    val itemAdded : LiveData<Boolean>
        get() = _itemAdded


    init {
        _imageResource.value = convertTypeTaskToResourceString(taskType)
    }

    fun onOkButtonClicked(name : String) {
        if (name.isEmpty()) {
            _emptyString.value = true
            return
        }

        viewModelScope.launch {
            repository.insertTask(TaskGridItem(name, _imageResource.value!!, taskType))
            _itemAdded.value = true
        }
    }

    fun onEmptyEventCompleted() {
        _emptyString.value = false
    }

    fun onDatabaseErrorCompleted() {
        _databaseError.value = false
    }

    fun onItemAddedCompleted() {
        _itemAdded.value = false
    }

    class Factory(val app: Application, val taskType: TaskTypes) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TaskConfigurationViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TaskConfigurationViewModel(app, taskType) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}
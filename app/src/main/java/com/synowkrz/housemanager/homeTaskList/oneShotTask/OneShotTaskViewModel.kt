package com.synowkrz.housemanager.homeTaskList.oneShotTask

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.synowkrz.housemanager.R
import com.synowkrz.housemanager.TAG
import com.synowkrz.housemanager.homeTaskList.model.OneCategory
import com.synowkrz.housemanager.homeTaskList.model.OneShotTask
import com.synowkrz.housemanager.repository.HouseRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class OneShotTaskViewModel @Inject constructor(val repository: HouseRepository, val app : Application) : AndroidViewModel(app) {

    var toDoList : LiveData<List<OneShotTask>> = repository.oneTaskToDo
    var doneList : LiveData<List<OneShotTask>> = repository.oneTaskDone


    private val _newTak = MutableLiveData<Boolean>()
    val newTask : LiveData<Boolean> = _newTak

    fun onNewTask() {
        _newTak.value = true
    }

    fun insertNewTask(oneShotTask: OneShotTask) {
        viewModelScope.launch {
            repository.inserNewOneShotTask(oneShotTask)
        }
    }

    fun updateTask(oneShotTask: OneShotTask) {
        viewModelScope.launch {
            repository.updateOneShotTask(oneShotTask)
        }
    }

    fun deleteTask(oneShotTask: OneShotTask) {
        viewModelScope.launch {
            repository.deleteOneShotTask(oneShotTask)
        }
    }

    fun changeDataSource(category : String) {

        if (category == app.getString(R.string.all_categories)) {
            toDoList = repository.oneTaskToDo
            doneList =repository.oneTaskDone
        } else {

            var one = getCategoryFromString(category)
            toDoList = repository.getAllOneTaskToDoByCategory(one)
            doneList = repository.getAllOneTaskDoneByCategory(one)
        }


    }


    fun getCategoryFromString(category: String) : OneCategory {
        return when(category) {
            app.getString(R.string.kids_category) -> OneCategory.KIDS
            app.getString(R.string.home_category) -> OneCategory.HOME
            app.getString(R.string.new_home_category) -> OneCategory.NEW_HOME
            app.getString(R.string.car_category) -> OneCategory.CAR
            app.getString(R.string.phone_category) -> OneCategory.PHONE
            app.getString(R.string.shop_category) -> OneCategory.SHOP
            else -> OneCategory.OTHER
        }
    }


}

package com.synowkrz.housemanager.babyTask.babyMainMenu

import android.app.Application
import androidx.lifecycle.*
import com.synowkrz.housemanager.babyTask.model.BabyProfile
import com.synowkrz.housemanager.repository.HouseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BabyMainMenuViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = HouseRepository(app)
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val itemList : LiveData<List<BabyProfile>>

    init {
        itemList = repository.babies
    }

    private val _addBaby = MutableLiveData<Boolean>()
    val addBaby: LiveData<Boolean>
        get() = _addBaby


    fun onAddBabyPressed() {
        _addBaby.value = true
    }

    fun onAddBabyFinished() {
        _addBaby.value = false
    }

    fun removeBabyProfile(babyProfile: BabyProfile) {
        viewModelScope.launch {
            repository.removeBabyProfile(babyProfile)
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BabyMainMenuViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return BabyMainMenuViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
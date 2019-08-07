package com.synowkrz.housemanager.babyTask.babyManager

import android.app.Application
import androidx.lifecycle.*
import com.synowkrz.housemanager.babyTask.model.BasicBabyEvent
import com.synowkrz.housemanager.babyTask.model.BabyProfile
import com.synowkrz.housemanager.babyTask.model.EventType
import com.synowkrz.housemanager.repository.HouseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BabyManagerViewModel(app: Application, val name: String) : AndroidViewModel(app) {

    private val repository = HouseRepository(app)
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _currentProflie = MutableLiveData<BabyProfile>()
    val currentProfile : LiveData<BabyProfile>
        get() = _currentProflie
    var currentProfileList : List<BabyProfile>? = null

    private val _itemList = MutableLiveData<List<BasicBabyEvent>>()

    val itemList : LiveData<List<BasicBabyEvent>>
            get() = _itemList

    init {
        initActions()
        viewModelScope.launch {
            currentProfileList = repository.getAllBabiesData()
            _currentProflie.value = repository.getBabyProfile(name)
        }
    }

    private fun initActions() {
        val eventList = mutableListOf<BasicBabyEvent>()
        enumValues<EventType>().forEach {
            eventList.add(BasicBabyEvent(it))
        }
        _itemList.value = eventList
    }

    class Factory(val app: Application, val name: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BabyManagerViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return BabyManagerViewModel(app, name) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
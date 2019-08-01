package com.synowkrz.housemanager.babyTask.babyCreator

import android.app.Application
import androidx.lifecycle.*

class BabyProfileCreatorViewModel(app : Application) : AndroidViewModel(app) {

    private val _photoClicked = MutableLiveData<Boolean>()
    val photoClicked : LiveData<Boolean>
        get() = _photoClicked

    fun onBabyPhotoClicked() {
        _photoClicked.value = true
    }

    fun onBabyPhotoClickedFinished() {
        _photoClicked.value = false
    }

    private val _dateClicked = MutableLiveData<Boolean>()
    val dateClicked : LiveData<Boolean>
        get() = _dateClicked

    fun onDateClicked() {
        _dateClicked.value = true
    }

    fun onDateClickedFinished() {
        _dateClicked.value = false
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BabyProfileCreatorViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return BabyProfileCreatorViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
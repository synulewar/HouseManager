package com.synowkrz.housemanager.babyTask.feeding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FeedingViewModel(app: Application): AndroidViewModel(app) {



    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FeedingViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FeedingViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
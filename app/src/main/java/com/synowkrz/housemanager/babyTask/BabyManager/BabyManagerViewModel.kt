package com.synowkrz.housemanager.babyTask.BabyManager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BabyManagerViewModel(app: Application) : AndroidViewModel(app) {


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BabyManagerViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return BabyManagerViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
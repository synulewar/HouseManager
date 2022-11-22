package com.synowkrz.housemanager.dagger

import androidx.annotation.NonNull
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(val viewModelsMap : Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>)  : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        @Suppress("UNCHECKED_CAST")
//        return  viewModelsMap.get(modelClass)!!.get() as T

        val creator = viewModelsMap[modelClass] ?:
        viewModelsMap.asIterable().firstOrNull {
            modelClass.isAssignableFrom(it.key)
        }?.value ?: throw IllegalArgumentException("unknown model class $modelClass")
        return try {
            creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }





}
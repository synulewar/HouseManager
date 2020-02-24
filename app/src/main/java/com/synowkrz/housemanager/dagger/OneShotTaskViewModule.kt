package com.synowkrz.housemanager.dagger

import androidx.lifecycle.ViewModel
import com.synowkrz.housemanager.homeTaskList.oneShotTask.OneShotTaskViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class OneShotTaskViewModule  {

    @Binds
    @IntoMap
    @ViewModelKey(OneShotTaskViewModel::class)
    abstract fun bindOneShotTaskViewModel(oneShotTaskViewModel: OneShotTaskViewModel) : ViewModel

}
package com.synowkrz.housemanager.dagger

import androidx.lifecycle.ViewModel
import com.synowkrz.housemanager.homeMenu.HomeMenuViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class HomeViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeMenuViewModel::class)
    abstract fun bindHomeMenuViewModel(homeMenuViewModel: HomeMenuViewModel) : ViewModel

}
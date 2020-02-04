package com.synowkrz.housemanager.dagger

import com.synowkrz.housemanager.homeMenu.HomeMenu
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ScreenBuilderModule {

    @ContributesAndroidInjector(modules = [HomeViewModelModule::class])
    abstract fun contributeHomeFragment() : HomeMenu

}
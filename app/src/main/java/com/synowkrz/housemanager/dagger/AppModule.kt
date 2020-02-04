package com.synowkrz.housemanager.dagger

import android.app.Application
import android.content.Context
import com.synowkrz.housemanager.repository.HouseRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideContext(application : Application): Context = application

    @Provides
    @Singleton
    fun providesRepository(context: Context) = HouseRepository(context)
}
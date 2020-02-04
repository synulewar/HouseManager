package com.synowkrz.housemanager.dagger

import android.app.Application

import com.synowkrz.housemanager.MainApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ScreenBuilderModule::class,
    ViewModelFactoryModule::class])
interface AppComponent : AndroidInjector<MainApp> {

    @Component.Builder
    interface Builder {

        fun build() : AppComponent

        @BindsInstance
        fun application(application: Application) : Builder
    }

}
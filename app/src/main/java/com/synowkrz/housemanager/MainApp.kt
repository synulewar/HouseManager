package com.synowkrz.housemanager


import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import com.synowkrz.housemanager.dagger.DaggerAppComponent
import com.synowkrz.housemanager.repository.HouseRepository
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject

class MainApp : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

    companion object {
        val UPDATE_DELAYED = 10_000L
    }

    @Inject
    lateinit var repository : HouseRepository


    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "App is created")

        //Register remote listeners get data from remote
        repository.registerShopListListener()
        repository.registerPersistentItemListener()
        repository.registerSchopAreaListener()
        repository.registerItemListListener()
        repository.registerHomeTaskListener()
        repository.registerDoneTaskListener()
        repository.registerOneShotListener()

        var handlerThread = HandlerThread("update thread")
        handlerThread.start()
        var handler = Handler(handlerThread.looper)
        handler.postDelayed({
            Log.d(TAG, "Schelude syncWithRemoteDatabase task")
            repository.syncWithRemoteDatabase()
            Log.d(TAG, "Finish syncWithRemoteDatabase")
        }, UPDATE_DELAYED)

// Removed for now
//        WorkManager.getInstance(applicationContext).cancelAllWork()
//        val notificatinWork = PeriodicWorkRequestBuilder<NotificationWorker>(3L, TimeUnit.HOURS)
//            .setInitialDelay(10000, TimeUnit.MILLISECONDS)
//            .build()
//
//
//        WorkManager.getInstance(applicationContext).cancelAllWork()

//        Log.d(TAG, "Schelude notification worker")
//        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork("HouseMenagerWorker",
//            ExistingPeriodicWorkPolicy.REPLACE, notificatinWork)
    }


}
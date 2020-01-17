package com.synowkrz.housemanager

import android.app.Application
import android.app.Notification
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.work.*
import com.synowkrz.housemanager.backgroundTask.NotificationWorker
import com.synowkrz.housemanager.repository.HouseRepository
import java.util.concurrent.TimeUnit

class MainApp : Application() {

    companion object {
        val UPDATE_DELAYED = 10_000L
    }

    private lateinit var repository : HouseRepository


    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "App is created")
        repository = HouseRepository(this)

        //Register remote listeners get data from remote
        repository.registerShopListListener()
        repository.registerPersistentItemListener()
        repository.registerSchopAreaListener()
        repository.registerItemListListener()
        repository.registerHomeTaskListener()
        repository.registerDoneTaskListener()

        var handlerThread = HandlerThread("update thread")
        handlerThread.start()
        var handler = Handler(handlerThread.looper)
        handler.postDelayed({
            Log.d(TAG, "Schelude syncWithRemoteDatabase task")
            repository.syncWithRemoteDatabase()
            Log.d(TAG, "Finish syncWithRemoteDatabase")
        }, UPDATE_DELAYED)


        val notificatinWork =PeriodicWorkRequestBuilder<NotificationWorker>(3L, TimeUnit.HOURS)
            .setInitialDelay(10000, TimeUnit.MILLISECONDS)
            .build()


        Log.d(TAG, "Schelude notification worker")
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork("HouseMenagerWorker",
            ExistingPeriodicWorkPolicy.REPLACE, notificatinWork)
    }


}
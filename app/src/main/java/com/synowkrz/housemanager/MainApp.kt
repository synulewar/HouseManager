package com.synowkrz.housemanager

import android.app.Application
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import com.synowkrz.housemanager.repository.HouseRepository

class MainApp : Application() {

    companion object {
        val UPDATE_DELAYED = 20_000L
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

        var handlerThread = HandlerThread("update thread")
        handlerThread.start()
        var handler = Handler(handlerThread.looper)
        handler.postDelayed({
            Log.d(TAG, "Schelude sendNewItemsToRemoteDatabase task")
            repository.sendNewItemsToRemoteDatabase()
        }, UPDATE_DELAYED)
    }
}
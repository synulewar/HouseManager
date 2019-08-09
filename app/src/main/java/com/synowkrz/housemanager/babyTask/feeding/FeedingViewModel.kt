package com.synowkrz.housemanager.babyTask.feeding

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.synowkrz.housemanager.*
import com.synowkrz.housemanager.babyTask.model.BabyEvent
import com.synowkrz.housemanager.babyTask.model.EventType
import com.synowkrz.housemanager.babyTask.model.Feeding
import com.synowkrz.housemanager.babyTask.model.FeedingType
import com.synowkrz.housemanager.repository.HouseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timerTask

class FeedingViewModel(val app: Application, val name: String): AndroidViewModel(app) {

    companion object {
        val UI_REFRESH_TIME : Long = 200
    }

    private val repository = HouseRepository(app)
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _feedingList : LiveData<List<Feeding>> = repository.getAllFeedingsByName(name)

    val itemList : LiveData<List<BabyEvent>> = Transformations.map(_feedingList, {
        var eventList = mutableListOf<BabyEvent>()
        it.forEach {
            var event = BabyEvent(EventType.FEED, it.startTime, it.profielName)
            event.endTime = it.endTime
            event.duration = it.duration
            event.subType = it.feedingType.toString()
            eventList.add(event)
        }
        eventList
    })


    private val _prepareFeedingScreen = MutableLiveData<FeedingType>()
    val prepareFeedingScreen : LiveData<FeedingType>
        get() = _prepareFeedingScreen

    private var mFeedingType : FeedingType? = null
        set(value) {
            _prepareFeedingScreen.value = value
            field = value
        }

    private val _feedingStateView = MutableLiveData<FeedingState>()
    val feedingStateView: LiveData<FeedingState>
        get() = _feedingStateView

    private var feedingState = FeedingState.STOPPED
        set(value) {
            _feedingStateView.value = value
            field = value
        }

    private val timer = Timer()
    private var timerTask: TimerTask? = null
    private var feedingStartTime : Long = 0L
    private var feedingVirtualTimeStamp = 0L
    private var feedingDuration = MutableLiveData<Long>()

    var convertTimeStampIntoString : (Long) -> String = {
        val minutes = it / 1000 / 60
        val seconds = it / 1000 % 60
        String.format("%02d:%02d", minutes, seconds)
    }

    val timerText : LiveData<String> = Transformations.map(feedingDuration, convertTimeStampIntoString)

    init {
        feedingDuration.value = 0L
        initializeFeedingState()
    }

    private fun initializeFeedingState() {
        val sharedPref = app.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        if (!sharedPref.getBoolean(BABY_FEED_ON_GOING + name, false)) {
            feedingState = FeedingState.STOPPED
            return
        }
        feedingStartTime = sharedPref.getLong(FEEDING_CURRENT_DURATION + name, 0L)
        mFeedingType = FeedingType.valueOf(sharedPref.getString(FEEDING_TYPE + name, "")!!)
        feedingState = FeedingState.valueOf(sharedPref.getString(FEEDING_CURRENT_STATE + name, "")!!)
        feedingState = FeedingState.valueOf(sharedPref.getString(FEEDING_CURRENT_STATE + name, "")!!)
        val lastFeedingDuration = sharedPref.getLong(FEEDING_CURRENT_DURATION + name, 0L)
        if (feedingState == FeedingState.PAUSED) {
            feedingDuration.value = lastFeedingDuration
        } else if (feedingState == FeedingState.STARTED) {
            feedingDuration.value = lastFeedingDuration + System.currentTimeMillis()- sharedPref.getLong(FEEDING_SAVE_TIMESTAMP + name, 0L)
            onFeedingRestarted()
        }
        sharedPref.edit().putBoolean(BABY_FEED_ON_GOING + name, false).apply()
    }

    fun onScreenPrepareFinished() {
        _feedingStateView.value = null
    }

    fun prepareFeeding(feedingType: FeedingType) {
        if (feedingState == FeedingState.STOPPED) {
            mFeedingType = feedingType
            feedingState = FeedingState.ACTIVE
        }
    }

    fun onStartPausePressed() {
        Log.d("KRZYSIO", "${feedingState.toString()}")
        if (feedingState == FeedingState.ACTIVE) {
            feedingState = FeedingState.STARTED
            feedingStartTime = System.currentTimeMillis()
            feedingVirtualTimeStamp = feedingStartTime
            scheludeTimerTask()
        } else if(feedingState == FeedingState.STARTED) {
            feedingState = FeedingState.PAUSED
            clearTask()

        } else if(feedingState == FeedingState.PAUSED) {
            onFeedingRestarted()
        }
    }

    fun onFeedingRestarted() {
        feedingState = FeedingState.STARTED
        feedingVirtualTimeStamp = System.currentTimeMillis() - feedingDuration.value!!
        scheludeTimerTask()
    }

    fun scheludeTimerTask() {
        timerTask = timerTask { feedingDuration.postValue(System.currentTimeMillis() - feedingVirtualTimeStamp) }
        timer.scheduleAtFixedRate(timerTask, 0L, UI_REFRESH_TIME)
    }

    fun onStopPressed() {
        feedingState = FeedingState.STOPPED
        viewModelScope.launch {
            val feeding = Feeding(feedingStartTime, name, mFeedingType!!, System.currentTimeMillis(), feedingDuration.value!!)
            repository.insertFeeding(feeding)
        }
        clearTask()
        feedingDuration.value = 0L
    }

    private fun clearTask() {
        timerTask?.cancel()
        timer.purge()
    }

    fun prepareFeedinScreenFinished() {
        _prepareFeedingScreen.value = null
    }

    override fun onCleared() {
        if (feedingState == FeedingState.STARTED || feedingState == FeedingState.PAUSED) {
            saveOnGoingFeeding(feedingStartTime, feedingDuration.value!!)
        }
        super.onCleared()
        clearTimer()
    }

    private fun saveOnGoingFeeding(startTime: Long, duration: Long) {
        val sharedPref = app.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        sharedPref.edit()
            .putBoolean(BABY_FEED_ON_GOING + name, true)
            .putLong(FEEDIG_START_TIME + name, startTime)
            .putLong(FEEDING_CURRENT_DURATION + name, duration)
            .putString(FEEDING_TYPE + name, mFeedingType.toString())
            .putString(FEEDING_CURRENT_STATE + name, feedingState.toString())
            .putLong(FEEDING_SAVE_TIMESTAMP + name, System.currentTimeMillis())
            .apply()
    }

    private fun clearTimer() {
        clearTask()
        timer.cancel()
    }

    class Factory(val app: Application, val profileName: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FeedingViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FeedingViewModel(app, profileName) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

enum class FeedingState {
    STARTED, STOPPED, PAUSED, ACTIVE
}
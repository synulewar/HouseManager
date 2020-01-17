package com.synowkrz.housemanager.homeTaskList.model

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.synowkrz.housemanager.TAG
import java.time.Duration
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Entity(tableName = "home_task")
data class HomeTask(@PrimaryKey val name : String ="",
                    var frequency : Int = 0,
                    var interval : Interval = Interval.DAYS,
                    var dueDate : String = "",
                    var daysExceeded : Int = 0,
                    var expired : Boolean = false)
{
    companion object {

        fun getFactor(interval : Interval) : Int {
            return when(interval) {
                Interval.DAYS -> 1
                Interval.WEEKS -> 7
                Interval.MONTHS -> 30
                Interval.YEARS -> 365
            }
        }


        fun calculateDueDate(timeStamp: LocalDate, inerval : Interval, frequency: Int) : LocalDate {
            val factor = getFactor(inerval)
            return timeStamp.plusDays((factor * frequency).toLong())
        }

        fun isTaskExpired(dueDate : LocalDate) : Boolean {
            return LocalDate.now() > dueDate
        }

        fun calculateDaysExceeded(dueDate: LocalDate) : Int {
            Log.d(TAG, "ChronoUnit.DAYS.between(LocalDate.now(), dueDate) = ${ChronoUnit.DAYS.between(LocalDate.now(), dueDate)}" )
            return ChronoUnit.DAYS.between(dueDate, LocalDate.now()).toInt()
        }
    }

    fun recalculateTask(frequency: Int, interval: Interval) {
        var oldDays = (getFactor(this.interval) * this.frequency).toLong()
        var newDays = (getFactor(interval) * frequency).toLong()
        var timeShift = newDays - oldDays
        Log.d(TAG, "Day difference new $newDays old $oldDays timeShift $timeShift oldDueDate ${this.dueDate}")
        this.dueDate = LocalDate.parse(this.dueDate).plusDays(timeShift).toString()
        this.daysExceeded = ChronoUnit.DAYS.between(LocalDate.parse(this.dueDate), LocalDate.now()).toInt()
        this.expired = LocalDate.now() > LocalDate.parse(this.dueDate)
        this.interval = interval
        this.frequency = frequency
        Log.d(TAG, "NewDueDate ${this.dueDate} daysExceeded ${this.daysExceeded} expired ${this.expired}")
    }

    fun doTask() {
        this.dueDate = LocalDate.now().plusDays((getFactor(this.interval) * this.frequency).toLong()).toString()
        Log.d(TAG, "New due date ${this.dueDate}")
    }
}


enum class Interval {
    DAYS, WEEKS, MONTHS, YEARS
}



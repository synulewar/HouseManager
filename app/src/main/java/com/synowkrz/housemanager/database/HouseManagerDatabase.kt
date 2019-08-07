package com.synowkrz.housemanager.database

import android.content.Context

import androidx.room.*
import com.synowkrz.housemanager.babyTask.model.EventType
import com.synowkrz.housemanager.babyTask.model.BabyProfile
import com.synowkrz.housemanager.babyTask.model.FeedingType
import com.synowkrz.housemanager.model.TaskGridItem
import com.synowkrz.housemanager.model.TaskTypes

@Database(entities = [TaskGridItem::class, BabyProfile::class], version = 1, exportSchema = false)
@TypeConverters(CustomConverters::class)
abstract class HouseManagerDatabase: RoomDatabase() {

    abstract val taskItemDao: TaskItemDao
    abstract val babyProfileDao: BabyProfileDao

    companion object {
        @Volatile
        private var INSTANCE: HouseManagerDatabase? = null

        fun getInstance(context: Context): HouseManagerDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        HouseManagerDatabase::class.java,
                        "house_manager_database")
                        .fallbackToDestructiveMigration()
                        .build()
                }
                INSTANCE = instance
                return instance
            }
        }
    }
}

class CustomConverters {
    @TypeConverter
    fun stringToTask (value : String) : TaskTypes {
        return TaskTypes.valueOf(value)
    }

    @TypeConverter
    fun taskToString(taskType: TaskTypes) : String {
        return taskType.toString()
    }

    @TypeConverter
    fun stringToFeedingType(value : String) : FeedingType {
        return FeedingType.valueOf(value)
    }

    @TypeConverter
    fun feedingTypeToString(feedingType: FeedingType) : String {
        return feedingType.toString()
    }

    @TypeConverter
    fun actionTypeToString(eventType: EventType) : String {
        return eventType.toString()
    }

    @TypeConverter
    fun stringToActionType(value: String) : EventType {
        return EventType.valueOf(value)
    }
}
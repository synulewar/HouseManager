package com.synowkrz.housemanager.database

import android.content.Context
import androidx.room.*
import com.synowkrz.housemanager.babyTask.model.BabyProfile
import com.synowkrz.housemanager.babyTask.model.EventType
import com.synowkrz.housemanager.babyTask.model.Feeding
import com.synowkrz.housemanager.babyTask.model.FeedingType
import com.synowkrz.housemanager.homeTaskList.model.DoneTask
import com.synowkrz.housemanager.homeTaskList.model.HomeTask
import com.synowkrz.housemanager.homeTaskList.model.Interval
import com.synowkrz.housemanager.model.TaskGridItem
import com.synowkrz.housemanager.model.TaskTypes
import com.synowkrz.housemanager.shopList.model.*
import java.time.LocalDate

@Database(entities = [TaskGridItem::class, BabyProfile::class, Feeding::class, ShopList::class,
    PersistentShopItem::class, ShopItem::class, ShopArea::class, HomeTask::class, DoneTask::class],
    version = 10, exportSchema = false)
@TypeConverters(CustomConverters::class)
abstract class HouseManagerDatabase: RoomDatabase() {

    abstract val taskItemDao: TaskItemDao
    abstract val babyProfileDao: BabyProfileDao
    abstract val feedingDao: FeedingDao
    abstract val shopListDao: ShopListDao
    abstract val persistentShopItemDao : PersistentShopItemDao
    abstract val shopItemDao : ShopItemDao
    abstract val shopAreaDao : ShopAreaDao
    abstract val homeTaskDao : HomeTaskDao
    abstract val doneTaskDao : DoneTaskDao

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

    @TypeConverter
    fun measurmentsToString(measurements: Measurements) : String {
        return measurements.toString()
    }

    @TypeConverter
    fun stringToMeasurment(value: String) : Measurements {
        return Measurements.valueOf(value)
    }

    @TypeConverter
    fun categoryToString(category: Category) : String {
        return category.toString()
    }

    @TypeConverter
    fun stringToCategory(value: String) : Category {
        return Category.valueOf(value)
    }

    @TypeConverter
    fun intervalToString(value: Interval) : String {
        return value.toString()
    }

    @TypeConverter
    fun stringToInterval(value: String) : Interval {
        return Interval.valueOf(value)
    }

    @TypeConverter
    fun dateToString(value: LocalDate) : String {
        return value.toString()
    }

    @TypeConverter
    fun stringToDate(value: String) : LocalDate {
        return LocalDate.parse(value)
    }
}
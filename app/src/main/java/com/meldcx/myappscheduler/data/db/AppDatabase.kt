package com.meldcx.myappscheduler.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.meldcx.myappscheduler.data.dao.AppEventDao
import com.meldcx.myappscheduler.data.dao.AppScheduleDao
import com.meldcx.myappscheduler.datamodel.model.AppEvent
import com.meldcx.myappscheduler.datamodel.model.AppSchedule
import com.meldcx.myappscheduler.util.Constants.DB_VERSION


@Database(
    entities = [AppSchedule::class, AppEvent::class],
    version = DB_VERSION,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getAppScheduleDao(): AppScheduleDao
    abstract fun getAppEventDao(): AppEventDao
}
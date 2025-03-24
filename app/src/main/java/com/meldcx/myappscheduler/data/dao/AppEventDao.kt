package com.meldcx.myappscheduler.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.meldcx.myappscheduler.datamodel.model.AppEvent

@Dao
interface AppEventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvent(appEvent: AppEvent)

    @Query("SELECT * FROM AppEvent Order By logTime Desc")
    fun getAllEvents(): List<AppEvent>

    @Query("DELETE FROM AppEvent WHERE id = :scheduleId")
    fun deleteEventsByScheduleId(scheduleId: Int)
}
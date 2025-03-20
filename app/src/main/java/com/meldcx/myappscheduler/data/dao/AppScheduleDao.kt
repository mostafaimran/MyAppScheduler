package com.meldcx.myappscheduler.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.meldcx.myappscheduler.datamodel.model.AppSchedule

@Dao
interface AppScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSchedule(appSchedule: AppSchedule)

    @Delete
    fun deleteSchedule(appSchedule: AppSchedule)

    @Query("SELECT * FROM AppSchedule")
    fun getAllSchedules(): List<AppSchedule>

    @Query("SELECT * FROM AppSchedule WHERE id = :id LIMIT 1")
    fun getSchedule(id: Int): AppSchedule
}
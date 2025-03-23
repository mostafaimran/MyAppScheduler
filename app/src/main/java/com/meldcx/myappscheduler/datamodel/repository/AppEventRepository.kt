package com.meldcx.myappscheduler.datamodel.repository

import com.meldcx.myappscheduler.datamodel.model.AppEvent

interface AppEventRepository {
    fun saveEvent(appEvent: AppEvent)
    fun getAllEvents(): List<AppEvent>
    fun deleteEventsByScheduleId(scheduleId: Int)
}
package com.meldcx.myappscheduler.data.repository

import com.meldcx.myappscheduler.data.dao.AppEventDao
import com.meldcx.myappscheduler.datamodel.model.AppEvent
import com.meldcx.myappscheduler.datamodel.repository.AppEventRepository
import javax.inject.Inject

class AppEventRepositoryImpl @Inject constructor(private val appEventDao: AppEventDao): AppEventRepository {

    override fun saveEvent(appEvent: AppEvent) {
        appEventDao.insertEvent(appEvent)
    }

    override fun getAllEvents(): List<AppEvent> {
        return appEventDao.getAllEvents()
    }

    override fun deleteEventsByScheduleId(scheduleId: Int) {
        appEventDao.deleteEventsByScheduleId(scheduleId)
    }
}
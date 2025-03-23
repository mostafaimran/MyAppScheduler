package com.meldcx.myappscheduler.data.repository

import com.meldcx.myappscheduler.data.dao.AppEventDao
import com.meldcx.myappscheduler.data.dao.AppScheduleDao
import com.meldcx.myappscheduler.datamodel.model.AppSchedule
import com.meldcx.myappscheduler.datamodel.repository.AppAlarmRepository
import com.meldcx.myappscheduler.datamodel.repository.AppSchedulerRepository
import javax.inject.Inject

class AppSchedulerRepositoryImpl @Inject constructor(
    private val appScheduleDao: AppScheduleDao,
    private val appEventDao: AppEventDao,
    private val alarmRepository: AppAlarmRepository,
) : AppSchedulerRepository {

    override fun scheduleApp(schedule: AppSchedule) {
        alarmRepository.setAlarm(
            schedule.id,
            schedule.packageName,
            schedule.scheduledTime,
            schedule.repeatDaily
        )
        appScheduleDao.insertSchedule(schedule)
    }

    override fun cancelSchedule(schedule: AppSchedule) {
        alarmRepository.cancelAlarm(schedule)
        appScheduleDao.deleteSchedule(schedule)
        appEventDao.deleteEventsByScheduleId(schedule.id)
    }

    override fun getAllSchedules() = appScheduleDao.getAllSchedules()

    override fun getSchedule(id: Int): AppSchedule? {
        return appScheduleDao.getSchedule(id)
    }
}
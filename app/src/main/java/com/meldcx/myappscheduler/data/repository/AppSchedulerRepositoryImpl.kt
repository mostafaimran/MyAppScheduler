package com.meldcx.myappscheduler.data.repository

import com.meldcx.myappscheduler.data.dao.AppScheduleDao
import com.meldcx.myappscheduler.datamodel.model.AppSchedule
import com.meldcx.myappscheduler.datamodel.repository.AppAlarmRepository
import com.meldcx.myappscheduler.datamodel.repository.AppSchedulerRepository
import javax.inject.Inject

class AppSchedulerRepositoryImpl @Inject constructor(
    private val dao: AppScheduleDao,
    private val alarmRepository: AppAlarmRepository,
) : AppSchedulerRepository {

    override fun scheduleApp(schedule: AppSchedule) {
        alarmRepository.setAlarm(
            schedule.id,
            schedule.packageName,
            schedule.scheduledTime,
            schedule.repeatDaily
        )
        dao.insertSchedule(schedule)
    }

    override fun cancelSchedule(schedule: AppSchedule) {
        alarmRepository.cancelAlarm(schedule)
        dao.deleteSchedule(schedule)
    }

    override fun getAllSchedules() = dao.getAllSchedules()

    override fun getSchedule(id: Int): AppSchedule? {
        return dao.getSchedule(id)
    }
}
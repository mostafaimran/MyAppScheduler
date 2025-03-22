package com.meldcx.myappscheduler.data.repository

import android.content.Context
import com.meldcx.myappscheduler.data.dao.AppScheduleDao
import com.meldcx.myappscheduler.datamodel.model.AppSchedule
import com.meldcx.myappscheduler.datamodel.repository.AppSchedulerRepository
import com.meldcx.myappscheduler.util.cancelAlarm
import com.meldcx.myappscheduler.util.setAlarm
import javax.inject.Inject

class AppSchedulerRepositoryImpl @Inject constructor(
    private val context: Context,
    private val dao: AppScheduleDao,
) : AppSchedulerRepository {

    override fun scheduleApp(schedule: AppSchedule) {
        context.setAlarm(schedule.id, schedule.packageName, schedule.scheduledTime)
        dao.insertSchedule(schedule)
    }

    override fun cancelSchedule(schedule: AppSchedule) {
        context.cancelAlarm(schedule)
        dao.deleteSchedule(schedule)
    }

    override fun getAllSchedules() = dao.getAllSchedules()

    override fun getSchedule(id: Int): AppSchedule? {
        return dao.getSchedule(id)
    }
}
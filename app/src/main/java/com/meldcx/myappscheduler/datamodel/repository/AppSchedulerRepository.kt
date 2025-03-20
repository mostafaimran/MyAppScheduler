package com.meldcx.myappscheduler.datamodel.repository

import com.meldcx.myappscheduler.datamodel.model.AppSchedule

interface AppSchedulerRepository {
    fun scheduleApp(schedule: AppSchedule)
    fun cancelSchedule(schedule: AppSchedule)
    fun getAllSchedules(): List<AppSchedule>
    fun getSchedule(id: Int): AppSchedule?
}
package com.meldcx.myappscheduler.datamodel.repository

import com.meldcx.myappscheduler.datamodel.model.AppSchedule

interface AppAlarmRepository {
    fun setAlarm(id: Int, packageName: String, scheduledTime: Long, repeatDaily: Boolean)
    fun cancelAlarm(schedule: AppSchedule)
}
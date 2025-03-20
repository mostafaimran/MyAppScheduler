package com.meldcx.myappscheduler.data.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.meldcx.myappscheduler.data.dao.AppScheduleDao
import com.meldcx.myappscheduler.datamodel.model.AppSchedule
import com.meldcx.myappscheduler.datamodel.repository.AppSchedulerRepository
import com.meldcx.myappscheduler.receiver.AppLaunchReceiver
import com.meldcx.myappscheduler.util.Extras
import com.meldcx.myappscheduler.util.getWithMutability
import javax.inject.Inject

class AppSchedulerRepositoryImpl @Inject constructor(
    private val context: Context,
    private val dao: AppScheduleDao,
) : AppSchedulerRepository {

    override fun scheduleApp(schedule: AppSchedule) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AppLaunchReceiver::class.java).apply {
            putExtra(Extras.EXTRA_PACKAGE, schedule.packageName)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, schedule.id, intent, PendingIntent.FLAG_UPDATE_CURRENT.getWithMutability()
        )
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, schedule.scheduledTime, pendingIntent)
        dao.insertSchedule(schedule)
    }

    override fun cancelSchedule(schedule: AppSchedule) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AppLaunchReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, schedule.id, intent, PendingIntent.FLAG_UPDATE_CURRENT.getWithMutability()
        )
        alarmManager.cancel(pendingIntent)
        dao.deleteSchedule(schedule)
    }

    override fun getAllSchedules() = dao.getAllSchedules()

    override fun getSchedule(id: Int): AppSchedule? {
        return dao.getSchedule(id)
    }
}
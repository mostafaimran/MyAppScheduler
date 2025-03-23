package com.meldcx.myappscheduler.data.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.meldcx.myappscheduler.datamodel.model.AppSchedule
import com.meldcx.myappscheduler.datamodel.repository.AppAlarmRepository
import com.meldcx.myappscheduler.receiver.AppLaunchReceiver
import com.meldcx.myappscheduler.util.Extras
import com.meldcx.myappscheduler.util.getScheduleTimeFormat
import com.meldcx.myappscheduler.util.getWithMutability
import java.util.Calendar
import javax.inject.Inject

class AppAlarmRepositoryImpl @Inject constructor(
    private val context: Context,
    private val alarmManager: AlarmManager,
) : AppAlarmRepository {

    companion object {
        private const val TAG = "AppAlarmRepositoryImpl"
    }

    override fun setAlarm(id: Int, packageName: String, scheduledTime: Long) {
        val intent = Intent(context, AppLaunchReceiver::class.java).apply {
            putExtra(Extras.EXTRA_ALARM_ID, id)
            putExtra(Extras.EXTRA_PACKAGE_NAME, packageName)
            putExtra(Extras.EXTRA_ALARM_TIME, scheduledTime)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT.getWithMutability()
        )

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = scheduledTime

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1)
        }

        Log.d(TAG, "Setting alarm for $packageName at ${calendar.time.getScheduleTimeFormat()}")

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent
        )
    }

    override fun cancelAlarm(schedule: AppSchedule) {
        val intent = Intent(context, AppLaunchReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, schedule.id, intent, PendingIntent.FLAG_UPDATE_CURRENT.getWithMutability()
        )

        Log.d(TAG, "Cancelling alarm for ${schedule.packageName}")

        alarmManager.cancel(pendingIntent)
    }
}
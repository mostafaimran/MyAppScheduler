package com.meldcx.myappscheduler.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.meldcx.myappscheduler.datamodel.repository.AppAlarmRepository
import com.meldcx.myappscheduler.util.Extras
import com.meldcx.myappscheduler.util.getAppIntent
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class AppLaunchReceiver : BroadcastReceiver() {
    companion object {
        private const val TAG = "AppLaunchReceiver"
    }

    @Inject
    lateinit var alarmRepository: AppAlarmRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        val packageName = intent?.getStringExtra(Extras.EXTRA_PACKAGE_NAME) ?: return

        val id = intent.getIntExtra(Extras.EXTRA_ALARM_ID, -1)
        val alarmTime = intent.getLongExtra(Extras.EXTRA_ALARM_TIME, -1)

        if (alarmTime != -1L) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = alarmTime
            calendar.add(Calendar.DATE, 1)
            alarmRepository.setAlarm(id, packageName, calendar.timeInMillis)
        }

        Log.d(TAG, "onReceive $packageName at ${Calendar.getInstance().time}")

        val launchIntent = context?.getAppIntent(packageName)
        launchIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(launchIntent)
    }
}
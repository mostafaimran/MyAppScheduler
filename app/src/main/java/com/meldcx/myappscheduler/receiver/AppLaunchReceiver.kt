package com.meldcx.myappscheduler.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.meldcx.myappscheduler.service.AppLaunchService
import com.meldcx.myappscheduler.util.Extras
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class AppLaunchReceiver : BroadcastReceiver() {
    companion object {
        private const val TAG = "AppLaunchReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val packageName = intent.getStringExtra(Extras.EXTRA_PACKAGE_NAME) ?: return

        val id = intent.getIntExtra(Extras.EXTRA_ALARM_ID, -1)
        val alarmTime = intent.getLongExtra(Extras.EXTRA_ALARM_TIME, -1)
        val repeatDaily = intent.getBooleanExtra(Extras.EXTRA_REPEAT_DAILY, false)

        Log.d(TAG, "onReceive $packageName at ${Calendar.getInstance().time}")

        AppLaunchService.startService(context, id, alarmTime, repeatDaily, packageName)
    }
}
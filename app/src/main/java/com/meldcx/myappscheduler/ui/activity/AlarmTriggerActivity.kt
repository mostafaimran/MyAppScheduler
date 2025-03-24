package com.meldcx.myappscheduler.ui.activity

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.meldcx.myappscheduler.datamodel.model.AppEvent
import com.meldcx.myappscheduler.datamodel.repository.AppAlarmRepository
import com.meldcx.myappscheduler.datamodel.repository.AppEventRepository
import com.meldcx.myappscheduler.util.Extras
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class AlarmTriggerActivity: ComponentActivity() {
    companion object {
        const val TAG = "AlarmTriggerActivity"

        fun startActivity(
            context: Context,
            id: Int,
            scheduledTime: Long,
            repeatDaily: Boolean,
            packageName: String,
        ) {
            val intent = Intent(context, AlarmTriggerActivity::class.java).apply {
                putExtra(Extras.EXTRA_ALARM_ID, id)
                putExtra(Extras.EXTRA_PACKAGE_NAME, packageName)
                putExtra(Extras.EXTRA_ALARM_TIME, scheduledTime)
                putExtra(Extras.EXTRA_REPEAT_DAILY, repeatDaily)

                addFlags(FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var alarmRepository: AppAlarmRepository

    @Inject
    lateinit var appEventRepository: AppEventRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val packageName = intent.getStringExtra(Extras.EXTRA_PACKAGE_NAME)
        if (packageName == null) {
            finish()
            return
        }

        val id = intent.getIntExtra(Extras.EXTRA_ALARM_ID, -1)
        val alarmTime = intent.getLongExtra(Extras.EXTRA_ALARM_TIME, -1)
        val repeatDaily = intent.getBooleanExtra(Extras.EXTRA_REPEAT_DAILY, false)

        Log.d(TAG, "onCreate $packageName at ${Calendar.getInstance().time}")

        appEventRepository.saveEvent(
            AppEvent(
                scheduleId = id,
                logTime = Calendar.getInstance().timeInMillis
            )
        )

        if (alarmTime != -1L && repeatDaily) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = alarmTime
            calendar.add(Calendar.DATE, 1)
            alarmRepository.setAlarm(id, packageName, calendar.timeInMillis, true)
        }

        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        launchIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        if (launchIntent != null) {
            startActivity(launchIntent)
        } else {
            Log.e(TAG, "Unable to find launch intent for $packageName")
        }

        finish()
    }
}
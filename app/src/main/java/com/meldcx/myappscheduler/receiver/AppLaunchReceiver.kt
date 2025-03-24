package com.meldcx.myappscheduler.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.meldcx.myappscheduler.R
import com.meldcx.myappscheduler.datamodel.repository.AppAlarmRepository
import com.meldcx.myappscheduler.datamodel.repository.AppEventRepository
import com.meldcx.myappscheduler.ui.activity.AlarmTriggerActivity
import com.meldcx.myappscheduler.util.Constants
import com.meldcx.myappscheduler.util.Extras
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class AppLaunchReceiver : BroadcastReceiver() {
    companion object {
        private const val TAG = "AppLaunchReceiver"
    }

    @Inject
    lateinit var alarmRepository: AppAlarmRepository

    override fun onReceive(context: Context, intent: Intent) {
        val packageName = intent.getStringExtra(Extras.EXTRA_PACKAGE_NAME) ?: return
        val id = intent.getIntExtra(Extras.EXTRA_ALARM_ID, -1)
        val alarmTime = intent.getLongExtra(Extras.EXTRA_ALARM_TIME, -1)
        val repeatDaily = intent.getBooleanExtra(Extras.EXTRA_REPEAT_DAILY, false)
        val appName = intent.getStringExtra(Extras.EXTRA_APP_NAME) ?: ""

        Log.d(TAG, "onReceive $packageName at ${Calendar.getInstance().time}")

        if (alarmTime != -1L && repeatDaily) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = alarmTime
            calendar.add(Calendar.DATE, 1)
            alarmRepository.setAlarm(id, appName, packageName, calendar.timeInMillis, true)
        }

        createNotification(context, id, appName, packageName)

    }

    private fun createNotification(
        context: Context,
        id: Int,
        appName: String,
        packageName: String
    ) {
        val title = context.getString(R.string.app_name)
        val text = context.getString(R.string.time_to_open_app, appName)

        val pendingIntent =
            PendingIntent.getActivity(
                context,
                100,
                AlarmTriggerActivity.getIntent(
                    context,
                    id,
                    packageName
                ),
                PendingIntent.FLAG_IMMUTABLE
            )

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = Constants.NOTIFICATION_CHANNEL_ID
            val channel =
                NotificationChannel(
                    channelId,
                    context.getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_LOW
                )
            notificationManager.createNotificationChannel(channel)

            NotificationCompat.Builder(
                context, channelId
            ).setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.alarm_icon)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
                .build()
        } else {
            NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.alarm_icon)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
                .build()
        }

        notificationManager.notify(Random.nextInt(), notification)
    }
}
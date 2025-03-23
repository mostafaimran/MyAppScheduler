package com.meldcx.myappscheduler.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.meldcx.myappscheduler.R
import com.meldcx.myappscheduler.datamodel.model.AppEvent
import com.meldcx.myappscheduler.datamodel.repository.AppAlarmRepository
import com.meldcx.myappscheduler.datamodel.repository.AppEventRepository
import com.meldcx.myappscheduler.util.Constants
import com.meldcx.myappscheduler.util.Extras
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Random
import javax.inject.Inject

@AndroidEntryPoint
class AppLaunchService : Service() {
    companion object {
        private const val TAG = "AppLaunchService"

        fun startService(
            context: Context,
            id: Int,
            scheduledTime: Long,
            repeatDaily: Boolean,
            packageName: String,
        ) {
            val intent = Intent(context, AppLaunchService::class.java).apply {
                putExtra(Extras.EXTRA_ALARM_ID, id)
                putExtra(Extras.EXTRA_PACKAGE_NAME, packageName)
                putExtra(Extras.EXTRA_ALARM_TIME, scheduledTime)
                putExtra(Extras.EXTRA_REPEAT_DAILY, repeatDaily)
            }
            ContextCompat.startForegroundService(context, intent)
        }
    }

    @Inject
    lateinit var alarmRepository: AppAlarmRepository

    @Inject
    lateinit var appEventRepository: AppEventRepository

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val packageName =
            intent?.getStringExtra(Extras.EXTRA_PACKAGE_NAME) ?: return START_NOT_STICKY

        val id = intent.getIntExtra(Extras.EXTRA_ALARM_ID, -1)
        val alarmTime = intent.getLongExtra(Extras.EXTRA_ALARM_TIME, -1)
        val repeatDaily = intent.getBooleanExtra(Extras.EXTRA_REPEAT_DAILY, false)

        Log.d(TAG, "onStartCommand $packageName at ${Calendar.getInstance().time}")

        startForegroundService()

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

        stopSelf()
        return START_NOT_STICKY
    }

    private fun startForegroundService() {
        val notification = createNotification()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            startForeground(
                Random().nextInt(),
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA or ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE
            )
        } else {
            startForeground(1, notification)
        }
    }

    private fun createNotification(): Notification {
        val title = getString(R.string.app_name)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = Constants.NOTIFICATION_CHANNEL_ID
            val channel =
                NotificationChannel(
                    channelId,
                    getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_LOW
                )
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)

            NotificationCompat.Builder(
                this, channelId
            ).setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
                .build()
        } else {
            NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
                .build()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
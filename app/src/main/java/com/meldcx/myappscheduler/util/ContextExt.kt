package com.meldcx.myappscheduler.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.meldcx.myappscheduler.datamodel.model.AppInfo
import com.meldcx.myappscheduler.datamodel.model.AppSchedule
import com.meldcx.myappscheduler.receiver.AppLaunchReceiver
import java.util.Calendar


fun Context.getUserInstalledApps(): List<AppInfo> {
    val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

    return apps.filter { app ->
        (app.flags and ApplicationInfo.FLAG_SYSTEM) == 0 // Exclude system apps
    }.map {
        val name = packageManager.getApplicationLabel(it).toString()
        val packageName = it.packageName

        AppInfo(name, packageName)
    }
}

fun Context.getAppInfo(packageName: String): AppInfo? {
    val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
    val name = packageManager.getApplicationLabel(applicationInfo).toString()

    return AppInfo(name, packageName)
}

fun Context.getAppIntent(packageName: String): Intent? {
    return packageManager?.getLaunchIntentForPackage(packageName)
}

fun Context.setAlarm(id: Int, packageName: String, scheduledTime: Long) {
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(this, AppLaunchReceiver::class.java).apply {
        putExtra(Extras.EXTRA_ALARM_ID, id)
        putExtra(Extras.EXTRA_PACKAGE_NAME, packageName)
        putExtra(Extras.EXTRA_ALARM_TIME, scheduledTime)
    }
    val pendingIntent = PendingIntent.getBroadcast(
        this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT.getWithMutability()
    )

    val calendar = Calendar.getInstance()
    calendar.timeInMillis = scheduledTime

    if (calendar.before(Calendar.getInstance())) {
        calendar.add(Calendar.DATE, 1)
    }
    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        pendingIntent
    )
}

fun Context.cancelAlarm(schedule: AppSchedule) {
    val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(this, AppLaunchReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        this, schedule.id, intent, PendingIntent.FLAG_UPDATE_CURRENT.getWithMutability()
    )
    alarmManager.cancel(pendingIntent)
}
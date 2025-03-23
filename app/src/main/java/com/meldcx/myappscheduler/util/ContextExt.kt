package com.meldcx.myappscheduler.util

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.meldcx.myappscheduler.datamodel.model.AppInfo


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


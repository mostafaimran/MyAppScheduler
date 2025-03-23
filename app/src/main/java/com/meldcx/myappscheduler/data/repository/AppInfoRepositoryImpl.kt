package com.meldcx.myappscheduler.data.repository

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.meldcx.myappscheduler.datamodel.model.AppInfo
import com.meldcx.myappscheduler.datamodel.repository.AppInfoRepository
import javax.inject.Inject

class AppInfoRepositoryImpl @Inject constructor(private val packageManager: PackageManager) :
    AppInfoRepository {

    override fun getUserInstalledApps(): List<AppInfo> {
        val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        return apps.filter { app ->
            (app.flags and ApplicationInfo.FLAG_SYSTEM) == 0 // Exclude system apps
        }.map {
            val name = packageManager.getApplicationLabel(it).toString()
            val packageName = it.packageName

            AppInfo(name, packageName)
        }
    }

    override fun getAppInfo(packageName: String): AppInfo {
        val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
        val name = packageManager.getApplicationLabel(applicationInfo).toString()

        return AppInfo(name, packageName)
    }

}
package com.meldcx.myappscheduler.datamodel.repository

import com.meldcx.myappscheduler.datamodel.model.AppInfo

interface AppInfoRepository {
    fun getUserInstalledApps(): List<AppInfo>
    fun getAppInfo(packageName: String): AppInfo
}
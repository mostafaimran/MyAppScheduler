package com.meldcx.myappscheduler.util

import android.content.Context
import com.meldcx.myappscheduler.R
import com.meldcx.myappscheduler.ui.viewmodel.TabType


fun TabType.getTitle(context: Context): String {
    return when (this.name) {
        TabType.Schedules.name -> context.getString(R.string.schedules)
        TabType.Events.name -> context.getString(R.string.events)
        else -> ""
    }
}
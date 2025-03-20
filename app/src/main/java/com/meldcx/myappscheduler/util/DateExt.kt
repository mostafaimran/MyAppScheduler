package com.meldcx.myappscheduler.util

import com.meldcx.myappscheduler.util.Constants.DISPLAY_TIME_ONLY
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.getScheduleTimeFormat(): String {
    val sdf = SimpleDateFormat(
        DISPLAY_TIME_ONLY,
        Locale.getDefault()
    )
    return sdf.format(this)
}
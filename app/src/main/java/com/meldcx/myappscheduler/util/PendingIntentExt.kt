package com.meldcx.myappscheduler.util

import android.app.PendingIntent
import android.os.Build

fun Int.getWithImmutability(): Int {
    var updatingFlag = this
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        updatingFlag = updatingFlag or PendingIntent.FLAG_IMMUTABLE
    }

    return updatingFlag
}

fun Int.getWithMutability(): Int {
    var updatingFlag = this
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        updatingFlag = updatingFlag or PendingIntent.FLAG_MUTABLE
    }

    return updatingFlag
}
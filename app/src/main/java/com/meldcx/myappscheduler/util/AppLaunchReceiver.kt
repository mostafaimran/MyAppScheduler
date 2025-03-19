package com.meldcx.myappscheduler.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AppLaunchReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val packageName = intent?.getStringExtra(Extras.EXTRA_PACKAGE) ?: return
        val launchIntent = context?.packageManager?.getLaunchIntentForPackage(packageName)
        launchIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(launchIntent)
    }
}
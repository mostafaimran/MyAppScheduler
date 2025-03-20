package com.meldcx.myappscheduler.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.meldcx.myappscheduler.util.Extras

class AppLaunchReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val packageName = intent?.getStringExtra(Extras.EXTRA_PACKAGE) ?: return
        val launchIntent = context?.packageManager?.getLaunchIntentForPackage(packageName)
        launchIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(launchIntent)
    }
}
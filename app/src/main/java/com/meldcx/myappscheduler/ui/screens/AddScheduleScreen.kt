package com.meldcx.myappscheduler.ui.screens

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.meldcx.myappscheduler.R
import com.meldcx.myappscheduler.datamodel.model.AppSchedule
import com.meldcx.myappscheduler.ui.AppSchedulerViewModel
import java.util.Calendar

@Composable
fun AddScheduleScreen(viewModel: AppSchedulerViewModel, onScheduleAdded: () -> Unit) {
    var selectedApp by remember { mutableStateOf<String?>(null) }
    var hour by remember { mutableIntStateOf(0) }
    var minute by remember { mutableIntStateOf(0) }

    val context = LocalContext.current

    val packageManager = context.packageManager
    val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        .map { it.packageName }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = context.getString(R.string.select_app))
        LazyColumn(modifier = Modifier.height(200.dp)) {
            items(installedApps) { packageName ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedApp = packageName }
                        .padding(8.dp)) {
                    Text(text = packageName)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = context.getString(R.string.select_time))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { hour = (hour + 1) % 24 }) {
                Text(context.getString(R.string.hour, hour))
            }
            Button(onClick = { minute = (minute + 1) % 60 }) {
                Text(context.getString(R.string.minute, minute))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            selectedApp?.let {
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                }
                val schedule = AppSchedule(it.hashCode(), it, calendar.timeInMillis)
                viewModel.addSchedule(schedule)
                onScheduleAdded()
            }
        }) {
            Text(context.getString(R.string.schedule_app))
        }
    }
}
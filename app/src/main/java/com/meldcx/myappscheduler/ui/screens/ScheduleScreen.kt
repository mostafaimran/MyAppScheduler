package com.meldcx.myappscheduler.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.meldcx.myappscheduler.R
import com.meldcx.myappscheduler.ui.AppSchedulerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    viewModel: AppSchedulerViewModel = hiltViewModel(),
    onAddSchedule: () -> Unit
) {
    val schedules = viewModel.scheduleListState.schedules

    LaunchedEffect(Unit) {
        viewModel.loadSchedules()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAddSchedule()
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add schedule")
            }
        }) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (schedules.isNotEmpty()) {
                schedules.forEach { schedule ->
                    Row {
                        Text(schedule.packageName)
                        Button(onClick = { viewModel.cancelSchedule(schedule) }) {
                            Text(LocalContext.current.getString(R.string.cancel))
                        }
                    }
                }
            } else {
                Text(
                    LocalContext.current.getString(R.string.no_schedule_found),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
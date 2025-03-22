package com.meldcx.myappscheduler.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.meldcx.myappscheduler.R
import com.meldcx.myappscheduler.datamodel.model.AppSchedule
import com.meldcx.myappscheduler.ui.viewmodel.AppSchedulerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    viewModel: AppSchedulerViewModel = hiltViewModel(),
    onAddEditSchedule: (schedule: AppSchedule?) -> Unit,
) {
    val schedules = viewModel.scheduleListState.schedules
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.loadSchedules()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                title = {
                    Text(
                        text = stringResource(R.string.my_schedules),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAddEditSchedule(null)
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add schedule")
            }
        }) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (schedules.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.padding(16.dp)) {
                        items(schedules) { schedule ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.primary,
                                            RoundedCornerShape(16.dp)
                                        )
                                        .clickable {
                                            onAddEditSchedule(schedule)
                                        }
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = schedule.name,
                                        modifier = Modifier.align(alignment = Alignment.Center)
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        viewModel.cancelSchedule(schedule)
                                        viewModel.loadSchedules()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "delete schedule"
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                } else {
                    Text(
                        stringResource(R.string.no_schedule_found),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}
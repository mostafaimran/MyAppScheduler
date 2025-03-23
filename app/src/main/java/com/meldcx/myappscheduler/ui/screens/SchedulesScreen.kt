package com.meldcx.myappscheduler.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.meldcx.myappscheduler.datamodel.model.ScheduleAction
import com.meldcx.myappscheduler.ui.viewmodel.AppSchedulerViewModel
import com.meldcx.myappscheduler.util.getScheduleTimeFormat
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchedulesScreen(
    viewModel: AppSchedulerViewModel = hiltViewModel(),
    onAddEditSchedule: (schedule: AppSchedule?) -> Unit,
) {
    val schedules = viewModel.scheduleListState.schedules
    val lifecycleOwner = LocalLifecycleOwner.current

    val showDeleteScheduleDialog = remember { mutableStateOf<ScheduleAction.DeleteSchedule?>(null) }
    val showEditScheduleDialog = remember { mutableStateOf<ScheduleAction.EditSchedule?>(null) }

    if (showDeleteScheduleDialog.value?.showDialog == true) {
        ScheduleActionDialogScreen(
            title = stringResource(R.string.delete_schedule),
            message = stringResource(
                R.string.delete_schedule_confirm_text,
                showDeleteScheduleDialog.value!!.schedule!!.name
            ),
            onConfirm = {
                showDeleteScheduleDialog.value?.schedule?.let {
                    viewModel.cancelSchedule(it)
                    viewModel.loadSchedules()
                }

                showDeleteScheduleDialog.value = ScheduleAction.DeleteSchedule(false, null)
            },
            onDismiss = {
                showDeleteScheduleDialog.value = ScheduleAction.DeleteSchedule(false, null)
            }
        )
    }

    if (showEditScheduleDialog.value?.showDialog == true) {
        ScheduleActionDialogScreen(
            title = stringResource(R.string.edit_schedule),
            message = stringResource(
                R.string.edit_schedule_confirm_text,
                showEditScheduleDialog.value!!.schedule!!.name
            ),
            onConfirm = {
                showEditScheduleDialog.value?.schedule?.let { onAddEditSchedule(it) }

                showEditScheduleDialog.value = ScheduleAction.EditSchedule(false, null)
            },
            onDismiss = {
                showEditScheduleDialog.value = ScheduleAction.EditSchedule(false, null)
            }
        )
    }

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.loadSchedules()
        }
    }

    LaunchedEffect(viewModel.scheduleListState.loadSchedules) {
        viewModel.loadSchedules()
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (schedules.isNotEmpty()) {
                LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                    items(schedules) { schedule ->
                        val calendar = Calendar.getInstance()
                        calendar.timeInMillis = schedule.scheduledTime

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.primary,
                                        RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        showEditScheduleDialog.value =
                                            ScheduleAction.EditSchedule(
                                                showDialog = true,
                                                schedule = schedule
                                            )
                                    }
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = schedule.name,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = calendar.time.getScheduleTimeFormat(),
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    if (schedule.repeatDaily) {
                                        Text(text = " | ")
                                        Text(
                                            text = stringResource(R.string.daily),
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                    }
                                }

                            }
                            IconButton(
                                onClick = {
                                    showDeleteScheduleDialog.value =
                                        ScheduleAction.DeleteSchedule(true, schedule)
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
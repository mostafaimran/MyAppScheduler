package com.meldcx.myappscheduler.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meldcx.myappscheduler.R
import com.meldcx.myappscheduler.datamodel.model.AppInfo
import com.meldcx.myappscheduler.datamodel.model.AppSchedule
import com.meldcx.myappscheduler.ui.viewmodel.AddScheduleViewModel
import com.meldcx.myappscheduler.util.getAppInfo
import com.meldcx.myappscheduler.util.getScheduleTimeFormat
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduleScreen(
    viewModel: AddScheduleViewModel = hiltViewModel(),
    editedScheduleId: Int?,
    onBack: () -> Unit,
    onScheduleAdded: () -> Unit
) {

    val schedule = viewModel.scheduleState.schedule

    var selectedApp by remember { mutableStateOf<AppInfo?>(null) }
    var selectedTime by remember { mutableStateOf<Calendar?>(null) }
    var showSelection by remember { mutableStateOf(false) }
    var showTimeSelection by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState()

    if (schedule != null) {
        selectedApp = LocalContext.current.getAppInfo(packageName = schedule.packageName)
        selectedTime = Calendar.getInstance().apply { timeInMillis = schedule.scheduledTime }

        timePickerState.hour = selectedTime!!.get(Calendar.HOUR_OF_DAY)
        timePickerState.minute = selectedTime!!.get(Calendar.MINUTE)
    }

    if (editedScheduleId != null) {
        viewModel.loadSchedule(editedScheduleId)
    }

    if (showSelection) {
        AppSelectionScreen(
            onDismiss = {
                showSelection = false
            }, onSelectedApp = {
                selectedApp = it
                showSelection = false
            }
        )
    }

    if (showTimeSelection) {
        TimePickerDialog(
            onDismissRequest = { showTimeSelection = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showTimeSelection = false

                        selectedTime = Calendar.getInstance().apply {
                            set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                            set(Calendar.MINUTE, timePickerState.minute)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }
                    }
                ) { Text(stringResource(R.string.ok)) }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showTimeSelection = false
                    }
                ) { Text(stringResource(R.string.cancel)) }
            }
        )
        {
            TimePicker(state = timePickerState)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                title = {
                    Text(
                        text = stringResource(R.string.add_schedule),
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.selected_app),
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 60.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            showSelection = true
                        }
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (selectedApp != null) {
                        Text(
                            text = selectedApp?.name ?: "",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.select_an_app),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
                OutlinedButton(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.End),
                    onClick = { showSelection = true })
                {
                    Text(text = stringResource(R.string.select_app))
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.schedule_time),
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 60.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            showTimeSelection = true
                        }
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (selectedTime != null) {
                        Text(
                            text = selectedTime?.time?.getScheduleTimeFormat() ?: "",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.select_a_time),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
                OutlinedButton(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.End),
                    onClick = { showTimeSelection = true }
                ) {
                    Text(text = stringResource(R.string.select_time))
                }

                Spacer(modifier = Modifier.height(60.dp))
                Button(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .align(Alignment.CenterHorizontally),
                    onClick = {
                        if (selectedApp != null && selectedTime != null) {
                            viewModel.addSchedule(
                                AppSchedule(
                                    selectedApp!!.hashCode(),
                                    selectedApp!!.name,
                                    selectedApp!!.packageName,
                                    selectedTime!!.timeInMillis
                                )
                            )
                            onScheduleAdded()
                        }
                    }) {
                    Text(stringResource(R.string.save_schedule))
                }
            }
        }
    }
}
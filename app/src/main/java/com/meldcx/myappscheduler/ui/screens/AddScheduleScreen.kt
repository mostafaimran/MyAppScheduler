package com.meldcx.myappscheduler.ui.screens

import android.content.pm.PackageItemInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.meldcx.myappscheduler.R
import com.meldcx.myappscheduler.datamodel.model.AppSchedule
import com.meldcx.myappscheduler.ui.AppSchedulerViewModel
import com.meldcx.myappscheduler.util.getScheduleTimeFormat
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduleScreen(
    viewModel: AppSchedulerViewModel,
    editedScheduleId: Int?,
    onBack: () -> Unit,
    onScheduleAdded: () -> Unit
) {
    var selectedApp by remember { mutableStateOf<PackageItemInfo?>(null) }
    val selectedTime by remember { mutableStateOf<Calendar?>(null) }
    var showSelection by remember { mutableStateOf(false) }
    var showTimeSelection by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState()

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
                },
                actions = {
                    OutlinedButton(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        onClick = {
                            selectedApp?.let {
                                val calendar = Calendar.getInstance().apply {
                                    set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                                    set(Calendar.MINUTE, timePickerState.minute)
                                    set(Calendar.SECOND, 0)
                                }
                                val schedule = AppSchedule(
                                    it.hashCode(),
                                    it.packageName,
                                    calendar.timeInMillis
                                )
                                viewModel.addSchedule(schedule)
                                onScheduleAdded()
                            }
                        }) {
                        Text(stringResource(R.string.save_schedule))
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
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.selected_app),
                        modifier = Modifier.weight(0.3f),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Row(
                        modifier = Modifier
                            .weight(0.7f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.primary,
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
                                    text = selectedApp?.packageName ?: "",
                                    style = MaterialTheme.typography.bodyLarge,
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

                        Button(
                            modifier = Modifier
                                .padding(16.dp),
                            onClick = { showSelection = true })
                        {
                            Text(text = stringResource(R.string.select_app))
                        }
                    }

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

                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.schedule_time),
                        modifier = Modifier.weight(0.3f),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Row(
                        modifier = Modifier
                            .weight(0.7f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.primary,
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
                                    style = MaterialTheme.typography.bodyLarge,
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

                        Button(
                            modifier = Modifier
                                .padding(16.dp),
                            onClick = { showTimeSelection = true }
                        ) {
                            Text(text = stringResource(R.string.select_time))
                        }
                    }
                }

                if (showTimeSelection) {
                    TimePickerDialog(
                        onDismissRequest = { },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showTimeSelection = false
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
            }
        }
    }
}
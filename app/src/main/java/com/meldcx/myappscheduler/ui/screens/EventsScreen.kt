package com.meldcx.myappscheduler.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.meldcx.myappscheduler.ui.viewmodel.AppEventsViewModel
import com.meldcx.myappscheduler.util.getEventTimeFormat
import java.util.Calendar

@Composable
fun EventsScreen(viewModel: AppEventsViewModel = hiltViewModel()) {
    val loading = viewModel.scheduleEventState.loading
    val appScheduleEventDataList = viewModel.scheduleEventState.appScheduleEventDataList

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.loadAllEvents()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            if (appScheduleEventDataList.isNotEmpty()) {
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(appScheduleEventDataList) { eventData ->
                        val calendar = Calendar.getInstance()
                        calendar.timeInMillis = eventData.appEvent.logTime

                        Spacer(modifier = Modifier.height(8.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(16.dp)
                                )
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = eventData.schedule.name,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = calendar.time.getEventTimeFormat(),
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            } else {
                Text(
                    stringResource(R.string.no_events_found),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
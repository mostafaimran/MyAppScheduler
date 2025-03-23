package com.meldcx.myappscheduler.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meldcx.myappscheduler.datamodel.model.AppScheduleEventData
import com.meldcx.myappscheduler.domain.GetAllAppScheduleEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppEventsViewModel @Inject constructor(
    private val getAllAppScheduleEvents: GetAllAppScheduleEvents,
) : ViewModel() {

    var scheduleEventState by mutableStateOf(AppEventsScreenState())
        private set

    fun loadAllEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            scheduleEventState = scheduleEventState.copy(loading = true)
            val list = getAllAppScheduleEvents(Any())
            scheduleEventState =
                scheduleEventState.copy(appScheduleEventDataList = list, loading = false)
        }
    }
}

data class AppEventsScreenState(
    val appScheduleEventDataList: List<AppScheduleEventData> = emptyList(),
    val loading: Boolean = false,
)
package com.meldcx.myappscheduler.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meldcx.myappscheduler.datamodel.model.AppSchedule
import com.meldcx.myappscheduler.datamodel.repository.AppSchedulerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppSchedulerViewModel @Inject constructor(
    private val repository: AppSchedulerRepository
) : ViewModel() {

    var scheduleListState by mutableStateOf(ScheduleListState())
        private set

    fun loadSchedules() {
        viewModelScope.launch(Dispatchers.IO) {
            scheduleListState = scheduleListState.copy(schedules = repository.getAllSchedules())
        }
    }

    fun cancelSchedule(schedule: AppSchedule) {
        viewModelScope.launch(Dispatchers.IO) { repository.cancelSchedule(schedule) }
    }
}

data class ScheduleListState(val schedules: List<AppSchedule> = emptyList())

data class DeleteScheduleState(val showDialog: Boolean = false, val schedule: AppSchedule? = null)
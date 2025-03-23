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
    private val repository: AppSchedulerRepository,
) : ViewModel() {

    var scheduleListState by mutableStateOf(ScheduleListState())
        private set

    fun loadSchedules() {
        viewModelScope.launch(Dispatchers.IO) {
            scheduleListState = scheduleListState.copy(
                schedules = repository.getAllSchedules(),
                loadSchedules = false
            )
        }
    }

    fun cancelSchedule(schedule: AppSchedule) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.cancelSchedule(schedule)
            refreshSchedules()
        }
    }

    fun refreshSchedules() {
        scheduleListState = scheduleListState.copy(loadSchedules = true)
    }
}

data class ScheduleListState(
    val loadSchedules: Boolean = true,
    val schedules: List<AppSchedule> = emptyList(),
)

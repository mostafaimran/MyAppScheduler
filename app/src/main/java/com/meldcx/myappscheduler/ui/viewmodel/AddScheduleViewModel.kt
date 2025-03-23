package com.meldcx.myappscheduler.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meldcx.myappscheduler.datamodel.model.AppSchedule
import com.meldcx.myappscheduler.datamodel.repository.AppInfoRepository
import com.meldcx.myappscheduler.datamodel.repository.AppSchedulerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddScheduleViewModel @Inject constructor(
    private val appScheduleRepository: AppSchedulerRepository,
    private val appInfoRepository: AppInfoRepository,
) : ViewModel() {

    var scheduleState by mutableStateOf(AddScheduleState())
        private set

    fun loadSchedule(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val schedule = appScheduleRepository.getSchedule(id)
            scheduleState = scheduleState.copy(schedule = schedule)
        }
    }

    fun addSchedule(schedule: AppSchedule) {
        viewModelScope.launch(Dispatchers.IO) { appScheduleRepository.scheduleApp(schedule) }
    }

    fun getAppInfo(packageName: String) = appInfoRepository.getAppInfo(packageName)

    fun getAllInstalledApps() = appInfoRepository.getUserInstalledApps()
}

data class AddScheduleState(val schedule: AppSchedule? = null)
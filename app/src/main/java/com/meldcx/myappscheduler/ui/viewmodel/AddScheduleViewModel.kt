package com.meldcx.myappscheduler.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meldcx.myappscheduler.datamodel.model.AppInfo
import com.meldcx.myappscheduler.datamodel.model.AppSchedule
import com.meldcx.myappscheduler.datamodel.repository.AppInfoRepository
import com.meldcx.myappscheduler.datamodel.repository.AppSchedulerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import kotlin.random.Random

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
            if (schedule != null) {
                updateScheduleAppInfo(schedule)
            }
        }
    }

    fun addSchedule(schedule: AppSchedule) {
        viewModelScope.launch(Dispatchers.IO) { appScheduleRepository.scheduleApp(schedule) }
    }

    fun getAppInfo(packageName: String) = appInfoRepository.getAppInfo(packageName)

    fun getAllInstalledApps() = appInfoRepository.getUserInstalledApps()

    private fun updateScheduleAppInfo(appSchedule: AppSchedule) {
        viewModelScope.launch(Dispatchers.IO) {
            scheduleState = scheduleState.copy(
                id = appSchedule.id,
                selectedApp = getAppInfo(packageName = appSchedule.packageName),
                selectedTime = Calendar.getInstance()
                    .apply { timeInMillis = appSchedule.scheduledTime },
                repeatDaily = appSchedule.repeatDaily
            )
        }
    }

    fun updateSelectedApp(appInfo: AppInfo) {
        scheduleState = scheduleState.copy(selectedApp = appInfo)
    }

    fun updateSelectedTime(calendar: Calendar) {
        scheduleState = scheduleState.copy(selectedTime = calendar)
    }

    fun updateRepeatDaily(repeatDaily: Boolean) {
        scheduleState = scheduleState.copy(repeatDaily = repeatDaily)
    }
}

data class AddScheduleState(
    val id: Int = Random.nextInt(),
    val selectedApp: AppInfo? = null,
    val selectedTime: Calendar? = null,
    val repeatDaily: Boolean = false,
)
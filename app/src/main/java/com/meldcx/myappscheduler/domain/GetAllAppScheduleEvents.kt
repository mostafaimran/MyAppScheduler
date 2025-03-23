package com.meldcx.myappscheduler.domain

import com.meldcx.myappscheduler.core.UseCase
import com.meldcx.myappscheduler.datamodel.model.AppScheduleEventData
import com.meldcx.myappscheduler.datamodel.repository.AppEventRepository
import com.meldcx.myappscheduler.datamodel.repository.AppSchedulerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAllAppScheduleEvents @Inject constructor(
    private val appEventRepository: AppEventRepository,
    private val appSchedulerRepository: AppSchedulerRepository,
) : UseCase<Any, List<AppScheduleEventData>>() {

    override suspend fun execute(parameters: Any): List<AppScheduleEventData> =
        withContext(Dispatchers.IO) {
            val appEvents = appEventRepository.getAllEvents()

            val appScheduleEventDataList = appEvents.mapNotNull { appEvent ->
                val schedule = appSchedulerRepository.getSchedule(appEvent.scheduleId)
                schedule?.let { AppScheduleEventData(it, appEvent) }
            }

            appScheduleEventDataList
        }
}
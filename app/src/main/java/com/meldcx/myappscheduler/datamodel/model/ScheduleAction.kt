package com.meldcx.myappscheduler.datamodel.model

sealed class ScheduleAction(
    open val showDialog: Boolean = false,
    open val schedule: AppSchedule? = null
) {
    data class DeleteSchedule(
        override val showDialog: Boolean = false,
        override val schedule: AppSchedule? = null
    ) : ScheduleAction()

    data class EditSchedule(
        override val showDialog: Boolean = false,
        override val schedule: AppSchedule? = null
    ) : ScheduleAction()

}
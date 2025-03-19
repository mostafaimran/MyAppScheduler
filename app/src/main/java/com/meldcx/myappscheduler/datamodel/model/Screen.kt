package com.meldcx.myappscheduler.datamodel.model

sealed class Screen(val route: String) {
    data object Schedules: Screen("schedulesScreen")
    data object AddSchedule: Screen("addScheduleScreen")
}
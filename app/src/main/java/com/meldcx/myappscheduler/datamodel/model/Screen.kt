package com.meldcx.myappscheduler.datamodel.model

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object Schedules : Screen()

    @Serializable
    data class AddSchedule(val id: Int? = null) : Screen()
}
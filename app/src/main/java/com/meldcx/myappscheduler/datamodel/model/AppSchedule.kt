package com.meldcx.myappscheduler.datamodel.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AppSchedule(
    @PrimaryKey
    val id: Int,
    val name: String,
    val packageName: String,
    val scheduledTime: Long
)

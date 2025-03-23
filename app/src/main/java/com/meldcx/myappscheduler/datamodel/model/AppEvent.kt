package com.meldcx.myappscheduler.datamodel.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class AppEvent(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val scheduleId: Int,
    val logTime: Long,
)
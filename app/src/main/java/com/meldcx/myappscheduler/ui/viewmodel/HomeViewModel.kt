package com.meldcx.myappscheduler.ui.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    val navigationItems = listOf(
        NavigationItem.Schedules,
        NavigationItem.Events
    )

}

sealed class NavigationItem(val tabType: TabType, val icon: ImageVector) {
    object Schedules : NavigationItem(TabType.Schedules, Icons.Default.Notifications)
    object Events : NavigationItem(TabType.Events, Icons.Default.Done)
}

enum class TabType {
    Schedules, Events
}
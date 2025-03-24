package com.meldcx.myappscheduler.ui.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    val navigationItems = listOf(
        NavigationItem.Schedules,
        NavigationItem.Events
    )

    private val _homeScreenState = MutableStateFlow(HomeScreenUiState())
    val homeScreenState: StateFlow<HomeScreenUiState> = _homeScreenState.asStateFlow()

    fun onPermissionGranted() {
        _homeScreenState.update { it.copy(permissionGranted = true) }
    }
}

data class HomeScreenUiState(val permissionGranted: Boolean = false)

sealed class NavigationItem(val tabType: TabType, val icon: ImageVector) {
    object Schedules : NavigationItem(TabType.Schedules, Icons.Default.Notifications)
    object Events : NavigationItem(TabType.Events, Icons.Default.Done)
}

enum class TabType {
    Schedules, Events
}
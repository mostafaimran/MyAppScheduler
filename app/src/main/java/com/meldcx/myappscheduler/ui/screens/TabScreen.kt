package com.meldcx.myappscheduler.ui.screens

import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.window.core.layout.WindowWidthSizeClass
import com.meldcx.myappscheduler.R
import com.meldcx.myappscheduler.datamodel.model.AppSchedule
import com.meldcx.myappscheduler.ui.viewmodel.HomeViewModel
import com.meldcx.myappscheduler.ui.viewmodel.NavigationItem
import com.meldcx.myappscheduler.util.getTitle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddEditSchedule: (schedule: AppSchedule?) -> Unit,
    permissionDenied: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                actions = {
                    IconButton(onClick = {
                        onAddEditSchedule(null)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "add schedule"
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        HomeTabScreen(
            innerPadding = innerPadding,
            onAddEditSchedule = onAddEditSchedule,
            permissionDenied = permissionDenied
        )
    }
}

@Composable
fun HomeTabScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    innerPadding: PaddingValues,
    onAddEditSchedule: (schedule: AppSchedule?) -> Unit,
    permissionDenied: () -> Unit,
) {
    val homeScreenState = viewModel.homeScreenState.collectAsState().value
    val permissionGranted = homeScreenState.permissionGranted

    if (permissionGranted) {
        val currentPage = remember { mutableStateOf(viewModel.navigationItems.first()) }

        val windowWidthClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass

        NavigationSuiteScaffold(
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
            navigationSuiteColors = NavigationSuiteDefaults.colors(
                navigationRailContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                navigationBarContainerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
            navigationSuiteItems = {
                viewModel.navigationItems.forEach { screen ->
                    item(
                        modifier = Modifier.padding(16.dp),
                        selected = screen == currentPage.value,
                        onClick = {
                            currentPage.value = screen
                        },
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.tabType.getTitle(LocalContext.current)
                            )
                        },
                        label = {
                            Text(
                                text = screen.tabType.getTitle(LocalContext.current),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    )
                }
            },
            layoutType = when (windowWidthClass) {
                WindowWidthSizeClass.EXPANDED -> {
                    NavigationSuiteType.NavigationRail
                }

                WindowWidthSizeClass.MEDIUM -> {
                    NavigationSuiteType.NavigationBar
                }

                else -> {
                    NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
                        currentWindowAdaptiveInfo()
                    )
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                when (currentPage.value) {
                    NavigationItem.Schedules -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            SchedulesScreen { onAddEditSchedule(it) }
                        }
                    }

                    NavigationItem.Events -> {
                        EventsScreen()
                    }
                }
            }
        }
    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionScreen(
                permissionGranted = {
                    viewModel.onPermissionGranted()
                }, permissionDenied = {
                    permissionDenied()
                }
            )
        } else {
            viewModel.onPermissionGranted()
        }
    }
}
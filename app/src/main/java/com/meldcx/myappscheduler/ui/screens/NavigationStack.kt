package com.meldcx.myappscheduler.ui.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.meldcx.myappscheduler.R
import com.meldcx.myappscheduler.datamodel.model.Screen
import com.meldcx.myappscheduler.util.Extras.EXTRA_REFRESH_LIST

@Composable
fun NavigationStack(permissionDenied: () -> Unit) {
    val navController = rememberNavController()

    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Screen.Schedules) {
        composable<Screen.Schedules> {
            val loadSchedules = it.savedStateHandle.get<Boolean>(EXTRA_REFRESH_LIST) == true
            HomeScreen(
                loadSchedules = loadSchedules,
                onAddEditSchedule = {
                    navController.navigate(Screen.AddSchedule(it?.id))
                }, permissionDenied = {
                    permissionDenied()
                }
            )
        }

        composable<Screen.AddSchedule> {
            val args = it.toRoute<Screen.AddSchedule>()

            AddScheduleScreen(
                editedScheduleId = args.id,
                onBack = {
                    navController.popBackStack()
                }
            ) { schedule ->
                Toast.makeText(
                    context,
                    context.getString(R.string.app_schedule_saved, schedule.name),
                    Toast.LENGTH_SHORT
                ).show()

                navController.previousBackStackEntry?.savedStateHandle?.set(EXTRA_REFRESH_LIST, true)
                navController.popBackStack()

            }
        }
    }
}
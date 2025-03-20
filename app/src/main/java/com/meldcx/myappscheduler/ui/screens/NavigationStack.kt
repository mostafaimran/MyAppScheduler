package com.meldcx.myappscheduler.ui.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.meldcx.myappscheduler.R
import com.meldcx.myappscheduler.datamodel.model.Screen
import com.meldcx.myappscheduler.ui.AppSchedulerViewModel
import com.meldcx.myappscheduler.util.Extras.EXTRA_ID

@Composable
fun NavigationStack(viewModel: AppSchedulerViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Screen.Schedules.route) {
        composable(route = Screen.Schedules.route) {
            ScheduleScreen(viewModel = viewModel) {
                navController.navigate(Screen.AddSchedule.route)
            }
        }
        composable(
            route = Screen.AddSchedule.route + "?$EXTRA_ID={$EXTRA_ID}",
            arguments = listOf(
                navArgument(EXTRA_ID) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            val id = it.arguments?.getInt(EXTRA_ID)
            AddScheduleScreen(viewModel, editedScheduleId = id, onBack = {
                navController.popBackStack()
            }) {
                navController.popBackStack()

                Toast.makeText(
                    context,
                    context.getString(R.string.app_schedule_saved),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
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

@Composable
fun NavigationStack() {
    val navController = rememberNavController()

    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Screen.Schedules) {
        composable<Screen.Schedules> {
            ScheduleScreen {
                navController.navigate(Screen.AddSchedule(it?.id))
            }
        }

        composable<Screen.AddSchedule> {
            val args = it.toRoute<Screen.AddSchedule>()

            AddScheduleScreen(
                editedScheduleId = args.id,
                onBack = {
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
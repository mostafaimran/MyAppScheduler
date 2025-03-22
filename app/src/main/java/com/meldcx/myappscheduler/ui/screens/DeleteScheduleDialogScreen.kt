package com.meldcx.myappscheduler.ui.screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.meldcx.myappscheduler.R
import com.meldcx.myappscheduler.datamodel.model.AppSchedule

@Composable
fun DeleteScheduleDialogScreen(schedule: AppSchedule, onDelete: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = { Text(text = stringResource(R.string.delete_schedule)) },
        text = {
            Text(
                text = stringResource(
                    R.string.delete_schedule_confirm_text,
                    schedule.name
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDelete()
                }
            ) { Text(stringResource(R.string.ok)) }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) { Text(stringResource(R.string.cancel)) }
        }
    )
}
package com.meldcx.myappscheduler.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.meldcx.myappscheduler.datamodel.model.AppInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSelectionScreen(
    installedApps: List<AppInfo>,
    onDismiss: () -> Unit,
    onSelectedApp: (AppInfo) -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        }
    ) {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(installedApps) { appInfo ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
                        .clickable {
                            onSelectedApp(appInfo)
                        }
                        .padding(16.dp)

                ) {
                    Text(
                        text = appInfo.name,
                        modifier = Modifier.align(alignment = Alignment.Center)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
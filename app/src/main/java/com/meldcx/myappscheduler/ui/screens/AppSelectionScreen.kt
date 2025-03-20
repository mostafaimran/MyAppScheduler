package com.meldcx.myappscheduler.ui.screens

import android.content.pm.PackageItemInfo
import android.content.pm.PackageManager
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSelectionScreen(onDismiss: () -> Unit, onSelectedApp: (PackageItemInfo) -> Unit) {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        }
    ) {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(installedApps) { applicationInfo ->
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(16.dp)
                        ).padding(16.dp)
                        .clickable {
                            onSelectedApp (applicationInfo)
                        }
                ) {
                    Text(text = applicationInfo.packageName, modifier = Modifier.align(alignment = Alignment.Center))
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
package com.meldcx.myappscheduler.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.meldcx.myappscheduler.R
import com.meldcx.myappscheduler.ui.screens.NavigationStack
import com.meldcx.myappscheduler.ui.theme.MyAppSchedulerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppSchedulerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppSchedulerTheme {
                Surface(
                    modifier = Modifier.Companion.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationStack {
                        Toast.makeText(
                            this,
                            R.string.notification_permission_required,
                            Toast.LENGTH_SHORT
                        ).show()

                        finish()
                    }
                }
            }
        }
    }
}
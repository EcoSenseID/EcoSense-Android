package com.ecosense.android.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ecosense.android.NavGraphs
import com.ecosense.android.core.presentation.theme.EcoSenseTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcoSenseTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}
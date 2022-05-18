package com.ecosense.android.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import com.ecosense.android.NavGraphs
import com.ecosense.android.core.presentation.component.BottomBar
import com.ecosense.android.core.presentation.theme.EcoSenseTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.rememberNavHostEngine
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcoSenseTheme {
                // TODO: 16/05/2022
                //  add login state checking here, if logged in show the following scaffold
                // Create another NavGraph for that case
                // https://composedestinations.rafaelcosta.xyz/defining-navgraphs

                val engine = rememberNavHostEngine()
                val navController = engine.rememberNavController()
                Scaffold(
                    bottomBar = { BottomBar(navController = navController) },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)
                ) { scaffoldPadding ->
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        engine = engine,
                        navController = navController,
                        modifier = Modifier.padding(scaffoldPadding)
                    )
                }
            }
        }
    }
}
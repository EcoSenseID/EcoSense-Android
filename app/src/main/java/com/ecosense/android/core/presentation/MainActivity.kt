package com.ecosense.android.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.ecosense.android.NavGraphs
import com.ecosense.android.appCurrentDestinationAsState
import com.ecosense.android.core.presentation.component.BottomBar
import com.ecosense.android.core.presentation.component.BottomBarDestination
import com.ecosense.android.core.presentation.theme.EcoSenseTheme
import com.ecosense.android.startAppDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.navigateTo
import com.ramcosta.composedestinations.rememberNavHostEngine
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.isLoggedIn.value == null }
        }

        setContent {
            EcoSenseTheme {
                val engine = rememberNavHostEngine()
                val navController = engine.rememberNavController()

                when (viewModel.isLoggedIn.collectAsState().value) {
                    true -> {
                        Scaffold(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colors.background)
                        ) { scaffoldPadding ->
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(scaffoldPadding),
                            ) {
                                DestinationsNavHost(
                                    navGraph = NavGraphs.root,
                                    engine = engine,
                                    navController = navController,
                                    modifier = Modifier.weight(1f),
                                )

                                val currentDestination =
                                    navController.appCurrentDestinationAsState().value
                                        ?: NavGraphs.root.startAppDestination

                                if (BottomBarDestination.values()
                                        .any { it.direction == currentDestination }
                                ) {
                                    Divider()

                                    BottomBar(
                                        currentDestination = { currentDestination },
                                        onItemClick = { destination ->
                                            navController.navigateTo(destination.direction) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                    )
                                }
                            }
                        }
                    }
                    false -> {
                        Scaffold(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colors.background)
                        ) { scaffoldPadding ->
                            DestinationsNavHost(
                                navGraph = NavGraphs.auth,
                                engine = engine,
                                navController = navController,
                                modifier = Modifier.padding(scaffoldPadding)
                            )
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}
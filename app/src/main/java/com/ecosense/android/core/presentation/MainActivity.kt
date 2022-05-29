package com.ecosense.android.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ecosense.android.NavGraphs
import com.ecosense.android.core.presentation.component.BottomBar
import com.ecosense.android.core.presentation.theme.EcoSenseTheme
import com.ramcosta.composedestinations.DestinationsNavHost
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
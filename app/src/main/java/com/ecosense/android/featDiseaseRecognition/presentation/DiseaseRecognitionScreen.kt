package com.ecosense.android.featDiseaseRecognition.presentation

import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.featDiseaseRecognition.presentation.component.DiseaseRecognitionPermissionRequest
import com.ecosense.android.featDiseaseRecognition.presentation.component.DiseaseRecognitionPreviewView
import com.ecosense.android.featDiseaseRecognition.presentation.component.DiseaseRecognitionResultSection
import com.ecosense.android.featDiseaseRecognition.presentation.component.DiseaseRecognitionTopBar
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
@Composable
@Destination
fun DiseaseRecognitionScreen(
    navigator: DestinationsNavigator,
    viewModel: DiseaseRecognitionViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    val camPermission = rememberPermissionState(permission = Manifest.permission.CAMERA)
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.surface,
        topBar = {
            DiseaseRecognitionTopBar(
                onHistoryClick = { /* TODO: navigate to HistoryScreen */ },
            )
        },
    ) { scaffoldPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        ) {
            AnimatedVisibility(visible = !camPermission.hasPermission) {
                DiseaseRecognitionPermissionRequest(camPermission = camPermission)
            }

            AnimatedVisibility(visible = camPermission.hasPermission) {
                Box(
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    DiseaseRecognitionPreviewView(
                        modifier = Modifier.fillMaxSize(),
                        onAnalyze = { viewModel.onAnalyze(it) },
                    )

                    DiseaseRecognitionResultSection(
                        mainDiagnosis = state.mainDiagnosis,
                        diffDiagnoses = state.diffDiagnoses,
                    )
                }
            }
        }
    }
}
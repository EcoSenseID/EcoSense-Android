package com.ecosense.android.featRecognition.presentation.recognition

import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.destinations.SavedRecognitionResultsScreenDestination
import com.ecosense.android.featRecognition.presentation.component.DiseaseRecognitionPermissionRequest
import com.ecosense.android.featRecognition.presentation.component.DiseaseRecognitionPreviewView
import com.ecosense.android.featRecognition.presentation.component.DiseaseRecognitionResultSection
import com.ecosense.android.featRecognition.presentation.component.DiseaseRecognitionTopBar
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
@Composable
@Destination
fun DiseaseRecognitionScreen(
    navigator: DestinationsNavigator,
    viewModel: RecognitionViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    val camPermission = rememberPermissionState(permission = Manifest.permission.CAMERA)
    val scaffoldState = rememberScaffoldState()

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> {
                    focusManager.clearFocus()
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.uiText.asString(context)
                    )
                }

                is UIEvent.HideKeyboard -> {
                    focusManager.clearFocus()
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.surface,
        topBar = {
            DiseaseRecognitionTopBar(
                onHistoryClick = { navigator.navigate(SavedRecognitionResultsScreenDestination) },
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
                        onSaveResult = { viewModel.onSaveResult() }
                    )
                }
            }
        }
    }
}
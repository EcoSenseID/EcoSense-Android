package com.ecosense.android.featDiseaseRecognition.presentation

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.util.Size
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ecosense.android.R
import com.ecosense.android.core.presentation.component.RoundedEndsButton
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.featDiseaseRecognition.domain.DiseaseAnalyzer
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
@Destination
fun DiseaseRecognitionScreen(
    navigator: DestinationsNavigator,
    viewModel: DiseaseRecognitionViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val scaffoldState = rememberScaffoldState()
    val camPermission = rememberPermissionState(permission = Manifest.permission.CAMERA)

    var preview by remember { mutableStateOf<Preview?>(null) }
    var imageAnalyzer by remember { mutableStateOf<ImageAnalysis?>(null) }
    var camera by remember { mutableStateOf<Camera?>(null) }

    val cameraExecutor = Executors.newSingleThreadExecutor()

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            AnimatedVisibility(visible = !camPermission.hasPermission) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.large)
                ) {
                    Text(
                        text = stringResource(
                            id = when {
                                !camPermission.permissionRequested -> R.string.permission_request_message
                                camPermission.shouldShowRationale -> R.string.permission_request_rationale
                                else -> R.string.permission_request_denied_permanently
                            }
                        )
                    )

                    AsyncImage(
                        model = R.drawable.ic_robot_face_rafiki,
                        contentDescription = null,
                        modifier = Modifier.size(300.dp)
                    )

                    RoundedEndsButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            when {
                                !camPermission.permissionRequested -> camPermission.launchPermissionRequest()
                                camPermission.shouldShowRationale -> camPermission.launchPermissionRequest()
                                else -> {
                                    Intent(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", context.packageName, null)
                                    ).apply {
                                        this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        context.startActivity(this)
                                    }
                                }
                            }
                        }) {
                        Text(
                            text = stringResource(
                                id = when {
                                    !camPermission.permissionRequested -> R.string.grant_permission
                                    camPermission.shouldShowRationale -> R.string.grant_permission
                                    else -> R.string.open_app_settings
                                }
                            )
                        )
                    }
                }
            }

            AnimatedVisibility(visible = camPermission.hasPermission) {
                Box {
                    AndroidView(modifier = Modifier.fillMaxSize(), factory = { ctx ->
                        val previewView = PreviewView(ctx)

                        cameraProviderFuture.addListener({
                            // Used to bind the lifecycle of cameras to the lifecycle owner
                            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                            preview = Preview.Builder().build()

                            imageAnalyzer = ImageAnalysis.Builder()
                                .setTargetResolution(Size(224, 224))
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build()
                                .also { analysisUseCase: ImageAnalysis ->
                                    analysisUseCase.setAnalyzer(
                                        cameraExecutor,
                                        DiseaseAnalyzer(context) { items ->
                                            viewModel.updateData(
                                                items
                                            )
                                        }
                                    )
                                }

                            val cameraSelector =
                                if (cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA))
                                    CameraSelector.DEFAULT_BACK_CAMERA
                                else CameraSelector.DEFAULT_FRONT_CAMERA

                            try {
                                // Unbind use cases before rebinding
                                cameraProvider.unbindAll()

                                // Bind use cases to camera - try to bind everything at once and CameraX will find
                                // the best combination.
                                camera = cameraProvider.bindToLifecycle(
                                    lifecycleOwner, cameraSelector, preview, imageAnalyzer
                                )

                                // Attach the preview to preview view, aka View Finder
                                preview?.setSurfaceProvider(previewView.surfaceProvider)

                            } catch (exc: Exception) {
                                Log.e("TAG", "Use case binding failed", exc)
                            }

                        }, ContextCompat.getMainExecutor(context))

                        previewView
                    })

                    Row {
                        LazyColumn(modifier = Modifier.background(Color.Black)) {
                            items(state.recognisedDiseasesList.size) { i ->
                                val recognisedDisease = state.recognisedDiseasesList[i]
                                Text(text = recognisedDisease.toString(), color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
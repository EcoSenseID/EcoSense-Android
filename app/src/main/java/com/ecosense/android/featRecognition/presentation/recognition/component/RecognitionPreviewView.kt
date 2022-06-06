package com.ecosense.android.featRecognition.presentation.recognition.component

import android.graphics.Bitmap
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.ecosense.android.featRecognition.presentation.util.toBitmap
import logcat.asLog
import logcat.logcat
import java.util.concurrent.Executors

@Composable
fun RecognitionPreviewView(
    modifier: Modifier = Modifier,
    onAnalyze: (Bitmap) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraExecutor = Executors.newSingleThreadExecutor()

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    AndroidView(modifier = modifier, factory = {
        val previewView = PreviewView(context)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(224, 224))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .apply {
                    setAnalyzer(cameraExecutor) { imageProxy ->
                        val bitmap = imageProxy.toBitmap(context)
                        bitmap?.let { onAnalyze(it) }
                        imageProxy.close()
                    }
                }

            val cameraSelector =
                if (cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA))
                    CameraSelector.DEFAULT_BACK_CAMERA
                else CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview, imageAnalysis
                )
                preview.setSurfaceProvider(previewView.surfaceProvider)
            } catch (e: Exception) {
                logcat("DiseaseRecognitionPreviewView") { e.asLog() }
            }
        }, ContextCompat.getMainExecutor(context))

        previewView
    })
}
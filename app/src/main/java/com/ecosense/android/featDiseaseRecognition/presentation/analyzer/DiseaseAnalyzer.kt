package com.ecosense.android.featDiseaseRecognition.presentation.analyzer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.ecosense.android.featDiseaseRecognition.presentation.util.YuvToRgbConverter
import com.ecosense.android.featDiseaseRecognition.domain.model.RecognisedDisease
import com.ecosense.android.ml.FlowerModel
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.model.Model

class DiseaseAnalyzer(
    private val context: Context,
    private val onAnalysisResult: (recognisedDisease: List<RecognisedDisease>) -> Unit
) : ImageAnalysis.Analyzer {

    // Initializing the flowerModel by lazy so that it runs in the same thread when the process
    // method is called.
    private val flowerModel by lazy { FlowerModel.newInstance(context, options) }

    private val options: Model.Options = Model.Options.Builder()
        .apply {
            when {
                CompatibilityList().isDelegateSupportedOnThisDevice -> setDevice(Model.Device.GPU)
                else -> setNumThreads(4)
            }
        }.build()

    override fun analyze(imageProxy: ImageProxy) {
        val items = mutableListOf<RecognisedDisease>()

        val tfImage = TensorImage.fromBitmap(toBitmap(imageProxy))

        val outputs = flowerModel.process(tfImage)
            .probabilityAsCategoryList
            .apply { sortByDescending { it.score } }

        for (output in outputs) {
            items.add(RecognisedDisease(output.label, output.score))
        }

        onAnalysisResult(items.toList())

        // Close the image,this tells CameraX to feed the next image to the analyzer
        imageProxy.close()
    }

    private val yuvToRgbConverter = YuvToRgbConverter(context)
    private lateinit var bitmapBuffer: Bitmap
    private lateinit var rotationMatrix: Matrix

    private fun toBitmap(imageProxy: ImageProxy): Bitmap? {

        @ExperimentalGetImage
        val image = imageProxy.image ?: return null

        // Initialise Buffer
        if (!::bitmapBuffer.isInitialized) {
            // The image rotation and RGB image buffer are initialized only once
            rotationMatrix = Matrix()
            rotationMatrix.postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())
            bitmapBuffer = Bitmap.createBitmap(
                imageProxy.width, imageProxy.height, Bitmap.Config.ARGB_8888
            )
        }

        // Pass image to an image analyser
        yuvToRgbConverter.yuvToRgb(image, bitmapBuffer)

        // Create the Bitmap in the correct orientation
        return Bitmap.createBitmap(
            bitmapBuffer,
            0,
            0,
            bitmapBuffer.width,
            bitmapBuffer.height,
            rotationMatrix,
            false
        )
    }
}
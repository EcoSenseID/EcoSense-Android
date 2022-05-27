package com.ecosense.android.featRecognition.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy

private lateinit var bitmapBuffer: Bitmap
private lateinit var rotationMatrix: Matrix

fun ImageProxy.toBitmap(
    context: Context
): Bitmap? {

    @ExperimentalGetImage
    val image = this.image ?: return null

    // Initialise Buffer
    if (!::bitmapBuffer.isInitialized) {
        // The image rotation and RGB image buffer are initialized only once
        rotationMatrix = Matrix()
        rotationMatrix.postRotate(this.imageInfo.rotationDegrees.toFloat())
        bitmapBuffer = Bitmap.createBitmap(
            this.width, this.height, Bitmap.Config.ARGB_8888
        )
    }

    // Pass image to an image analyser
    YuvToRgbConverter(context).yuvToRgb(image, bitmapBuffer)

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
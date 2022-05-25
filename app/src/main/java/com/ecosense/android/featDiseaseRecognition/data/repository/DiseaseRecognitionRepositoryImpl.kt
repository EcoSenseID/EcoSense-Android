package com.ecosense.android.featDiseaseRecognition.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.ecosense.android.featDiseaseRecognition.domain.model.RecognisedDisease
import com.ecosense.android.featDiseaseRecognition.domain.repository.DiseaseRecognitionRepository
import com.ecosense.android.ml.FlowerModel
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.model.Model

class DiseaseRecognitionRepositoryImpl(
    appContext: Context
) : DiseaseRecognitionRepository {

    private val flowerModel by lazy {
        FlowerModel.newInstance(
            appContext,
            Model.Options.Builder().apply {
                when {
                    CompatibilityList().isDelegateSupportedOnThisDevice ->
                        setDevice(Model.Device.GPU)
                    else -> setNumThreads(4)
                }
            }.build()
        )
    }

    override fun analyzeDiseases(
        bitmap: Bitmap
    ): List<RecognisedDisease> {
        return flowerModel
            .process(TensorImage.fromBitmap(bitmap))
            .probabilityAsCategoryList
            .apply { sortByDescending { it.score } }
            .map { RecognisedDisease(label = it.label, confidence = it.score) }
    }
}
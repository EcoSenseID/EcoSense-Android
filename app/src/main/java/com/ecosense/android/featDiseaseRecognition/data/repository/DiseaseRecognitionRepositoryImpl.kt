package com.ecosense.android.featDiseaseRecognition.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.ecosense.android.featDiseaseRecognition.data.util.toRecognisedDisease
import com.ecosense.android.featDiseaseRecognition.domain.model.RecognisedDisease
import com.ecosense.android.featDiseaseRecognition.domain.repository.DiseaseRecognitionRepository
import com.ecosense.android.ml.PlantDiseaseModel
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.model.Model

class DiseaseRecognitionRepositoryImpl(
    appContext: Context
) : DiseaseRecognitionRepository {

    private val plantDiseaseModel by lazy {
        PlantDiseaseModel.newInstance(
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
        return plantDiseaseModel
            .process(TensorImage.fromBitmap(bitmap))
            .probabilityAsCategoryList
            .apply { sortByDescending { it.score } }
            .map { it.toRecognisedDisease() }
    }
}
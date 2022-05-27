package com.ecosense.android.featRecognition.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.ecosense.android.R
import com.ecosense.android.core.data.local.dao.SavedRecognitionResultDao
import com.ecosense.android.core.data.model.SavedRecognitionResultEntity
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featRecognition.data.util.toRecognisedDisease
import com.ecosense.android.featRecognition.domain.model.RecognitionResult
import com.ecosense.android.featRecognition.domain.model.SavedRecognitionResult
import com.ecosense.android.featRecognition.domain.repository.RecognitionRepository
import com.ecosense.android.ml.PlantDiseaseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.model.Model

class RecognitionRepositoryImpl(
    private val appContext: Context,
    private val savedRecognitionResultDao: SavedRecognitionResultDao
) : RecognitionRepository {

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
    ): List<RecognitionResult> {
        return plantDiseaseModel
            .process(TensorImage.fromBitmap(bitmap))
            .probabilityAsCategoryList
            .apply { sortByDescending { it.score } }
            .map { it.toRecognisedDisease() }
    }

    override fun getRecognitionHistoryList(): Flow<Resource<List<SavedRecognitionResult>>> = flow {
        emit(Resource.Loading())
        try {
            val historyList = savedRecognitionResultDao.findAll().map { it.toDiseaseHistoryItem() }
            emit(Resource.Success(historyList))
        } catch (e: Exception) {
            emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
        }
    }

    override suspend fun saveRecognitionResult(
        recognitionResult: RecognitionResult,
    ) {
        savedRecognitionResultDao.save(
            recognitionResult = SavedRecognitionResultEntity(
                label = recognitionResult.label.asString(appContext),
                confidencePercent = recognitionResult.confidencePercent,
                timeInMillis = System.currentTimeMillis()
            )
        )
    }

    override suspend fun unsaveRecognitionResult(
        recognitionResult: SavedRecognitionResult,
    ) {
        savedRecognitionResultDao.delete(recognitionResult.id)
    }
}
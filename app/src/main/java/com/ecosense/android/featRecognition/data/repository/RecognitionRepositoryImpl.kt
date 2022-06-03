package com.ecosense.android.featRecognition.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.ecosense.android.R
import com.ecosense.android.core.data.local.dao.SavedRecognisableDao
import com.ecosense.android.core.data.model.SavedRecognisableEntity
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featRecognition.data.util.toRecognisable
import com.ecosense.android.featRecognition.domain.model.Recognisable
import com.ecosense.android.featRecognition.domain.model.SavedRecognisable
import com.ecosense.android.featRecognition.domain.repository.RecognitionRepository
import com.ecosense.android.ml.PlantDiseaseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.model.Model

class RecognitionRepositoryImpl(
    private val appContext: Context,
    private val savedRecognisableDao: SavedRecognisableDao
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

    override fun recognise(
        bitmap: Bitmap
    ): List<Recognisable> {
        return plantDiseaseModel
            .process(TensorImage.fromBitmap(bitmap))
            .probabilityAsCategoryList
            .apply { sortByDescending { it.score } }
            .map { it.toRecognisable() }
    }

    override fun getSavedRecognisables(): Flow<Resource<List<SavedRecognisable>>> = flow {
        emit(Resource.Loading())
        try {
            val historyList = savedRecognisableDao.findAll().map { it.toSavedRecognisable() }
            emit(Resource.Success(historyList))
        } catch (e: Exception) {
            emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
        }
    }

    override suspend fun getSavedRecognisable(
        id: Int
    ): SavedRecognisable? {
        return savedRecognisableDao.find(id)?.toSavedRecognisable()
    }

    override suspend fun saveRecognisable(recognisable: Recognisable) {
        savedRecognisableDao.save(
            recognitionResult = SavedRecognisableEntity(
                label = recognisable.label.asString(appContext),
                timeInMillis = System.currentTimeMillis(),
                confidencePercent = recognisable.confidencePercent,
                symptoms = recognisable.symptoms,
                treatment = recognisable.treatments,
                preventiveMeasure = recognisable.preventiveMeasures,
            )
        )
    }

    override suspend fun unsaveRecognisable(savedRecognisable: SavedRecognisable) {
        savedRecognisableDao.delete(savedRecognisable.id)
    }
}
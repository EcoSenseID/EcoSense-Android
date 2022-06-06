package com.ecosense.android.featRecognition.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.ecosense.android.R
import com.ecosense.android.core.data.local.dao.SavedRecognisableDao
import com.ecosense.android.core.data.model.SavedRecognisableEntity
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.SimpleResource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featRecognition.data.source.DiseaseDataSource
import com.ecosense.android.featRecognition.domain.model.Disease
import com.ecosense.android.featRecognition.domain.model.Recognisable
import com.ecosense.android.featRecognition.domain.model.RecognisableDetail
import com.ecosense.android.featRecognition.domain.model.SavedRecognisable
import com.ecosense.android.featRecognition.domain.repository.RecognitionRepository
import com.ecosense.android.ml.PlantDiseaseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import logcat.asLog
import logcat.logcat
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.model.Model

class RecognitionRepositoryImpl(
    private val appContext: Context,
    private val savedRecognisableDao: SavedRecognisableDao,
    private val diseaseDataSource: DiseaseDataSource
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
            .map {
                val disease = diseaseDataSource.getDisease(it.label)
                Recognisable(
                    label = it.label,
                    confidencePercent = (it.score * 100).toInt(),
                    readableName = disease?.readableName
                )
            }
    }

    override fun getSavedRecognisables(): Flow<Resource<List<SavedRecognisable>>> = flow {
        emit(Resource.Loading())
        try {
            val historyList = savedRecognisableDao.findAll().map {
                val disease = diseaseDataSource.getDisease(it.label)
                SavedRecognisable(
                    id = it.id,
                    label = it.label,
                    timeInMillis = it.timeInMillis,
                    readableName = disease?.readableName,
                    confidencePercent = it.confidencePercent,
                )
            }
            emit(Resource.Success(historyList))
        } catch (e: Exception) {
            emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
        }
    }

    override fun getDisease(label: String): Disease? {
        return diseaseDataSource.getDisease(label)
    }

    override suspend fun saveRecognisable(
        recognisable: Recognisable
    ): Flow<SimpleResource> = flow {
        emit(Resource.Loading())
        try {
            val savedRow = savedRecognisableDao.save(
                recognitionResult = SavedRecognisableEntity(
                    label = recognisable.label,
                    timeInMillis = System.currentTimeMillis(),
                    confidencePercent = recognisable.confidencePercent,
                )
            )
            emit(
                if (savedRow != 0L) Resource.Success(Unit)
                else Resource.Error(UIText.StringResource(R.string.em_unknown))
            )
        } catch (e: Exception) {
            logcat { e.asLog() }
            emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
        }
    }

    override suspend fun unsaveRecognisable(
        recognisableDetail: RecognisableDetail
    ): Flow<SimpleResource> = flow {
        emit(Resource.Loading())
        try {
            val deletedRow = savedRecognisableDao.delete(recognisableDetail.id)
            emit(
                if (deletedRow != 0) Resource.Success(Unit)
                else Resource.Error(UIText.StringResource(R.string.em_unknown))
            )
        } catch (e: Exception) {
            logcat { e.asLog() }
            emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
        }
    }
}
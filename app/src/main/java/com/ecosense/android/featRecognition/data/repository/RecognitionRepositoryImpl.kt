package com.ecosense.android.featRecognition.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.ecosense.android.R
import com.ecosense.android.core.domain.api.AuthApi
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featDiscoverCampaign.data.model.BrowseCampaignDto
import com.ecosense.android.featRecognition.data.remote.RecognitionApi
import com.ecosense.android.featRecognition.data.source.DiseaseDataSource
import com.ecosense.android.featRecognition.domain.model.Disease
import com.ecosense.android.featRecognition.domain.model.Recognisable
import com.ecosense.android.featRecognition.domain.model.SavedRecognisable
import com.ecosense.android.featRecognition.domain.repository.RecognitionRepository
import com.ecosense.android.ml.PlantDiseaseModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import logcat.asLog
import logcat.logcat
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.model.Model
import retrofit2.HttpException
import java.io.IOException

class RecognitionRepositoryImpl(
    private val appContext: Context,
    private val recognitionApi: RecognitionApi,
    private val authApi: AuthApi,
    private val diseaseDataSource: DiseaseDataSource,
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
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            val response = recognitionApi.getSavedRecognisables(bearerToken = bearerToken)

            when {
                response.error == true -> emit(Resource.Error(
                    uiText = response.message?.let { UIText.DynamicString(it) }
                        ?: UIText.StringResource(R.string.em_unknown))
                )

                response.recognisables == null -> {
                    emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
                }

                else -> {
                    response.recognisables
                        .map {
                            val disease = diseaseDataSource.getDisease(it.label ?: "")
                            SavedRecognisable(
                                id = it.id,
                                label = it.label ?: "",
                                savedAt = it.savedAt ?: 0,
                                readableName = disease?.readableName,
                                confidencePercent = it.confidencePercent ?: 0,
                            )
                        }
                        .let { emit(Resource.Success(it)) }
                }
            }
        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<BrowseCampaignDto>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<BrowseCampaignDto>() {}.type
                        )
                        UIText.DynamicString(response.message!!)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.em_unknown)
                    }
                }

                is IOException -> UIText.StringResource(R.string.em_io_exception)

                else -> UIText.StringResource(R.string.em_unknown)

            }.let { emit(Resource.Error(it)) }
        }
    }

    override fun getDisease(label: String): Disease? {
        return diseaseDataSource.getDisease(label)
    }

    override suspend fun saveRecognisable(
        label: String,
        confidencePercent: Int
    ): Flow<Resource<Int>> = flow {
        emit(Resource.Loading())
        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            val response = recognitionApi.saveRecognisable(
                bearerToken = bearerToken,
                label = label,
                confidencePercent = confidencePercent
            )

            when {
                response.error == true -> emit(Resource.Error(
                    uiText = response.message?.let { UIText.DynamicString(it) }
                        ?: UIText.StringResource(R.string.em_unknown))
                )

                response.recognisableId == null -> {
                    emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
                }

                else -> emit(Resource.Success(response.recognisableId))
            }
        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<BrowseCampaignDto>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<BrowseCampaignDto>() {}.type
                        )
                        UIText.DynamicString(response.message!!)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.em_unknown)
                    }
                }

                is IOException -> UIText.StringResource(R.string.em_io_exception)

                else -> UIText.StringResource(R.string.em_unknown)

            }.let { emit(Resource.Error(it)) }
        }
    }
}
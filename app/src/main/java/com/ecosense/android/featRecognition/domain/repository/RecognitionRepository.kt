package com.ecosense.android.featRecognition.domain.repository

import android.graphics.Bitmap
import com.ecosense.android.core.util.Resource
import com.ecosense.android.featRecognition.domain.model.RecognitionResult
import com.ecosense.android.featRecognition.domain.model.SavedRecognitionResult
import kotlinx.coroutines.flow.Flow

interface RecognitionRepository {
    fun analyzeDiseases(
        bitmap: Bitmap
    ): List<RecognitionResult>

    fun getRecognitionHistoryList(): Flow<Resource<List<SavedRecognitionResult>>>

    suspend fun saveRecognitionResult(recognitionResult: RecognitionResult)

    suspend fun unsaveRecognitionResult(recognitionResult: SavedRecognitionResult)
}
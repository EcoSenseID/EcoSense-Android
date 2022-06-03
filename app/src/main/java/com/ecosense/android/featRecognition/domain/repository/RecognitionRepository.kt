package com.ecosense.android.featRecognition.domain.repository

import android.graphics.Bitmap
import com.ecosense.android.core.util.Resource
import com.ecosense.android.featRecognition.domain.model.Recognisable
import com.ecosense.android.featRecognition.domain.model.SavedRecognisable
import kotlinx.coroutines.flow.Flow

interface RecognitionRepository {
    fun recognise(
        bitmap: Bitmap
    ): List<Recognisable>

    fun getSavedRecognisables(): Flow<Resource<List<SavedRecognisable>>>

    suspend fun getSavedRecognisable(id: Int): SavedRecognisable?

    suspend fun saveRecognisable(recognisable: Recognisable)

    suspend fun unsaveRecognisable(savedRecognisable: SavedRecognisable)
}
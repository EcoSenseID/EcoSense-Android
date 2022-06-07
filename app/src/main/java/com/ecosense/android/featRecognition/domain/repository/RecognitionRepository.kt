package com.ecosense.android.featRecognition.domain.repository

import android.graphics.Bitmap
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.SimpleResource
import com.ecosense.android.featRecognition.domain.model.Disease
import com.ecosense.android.featRecognition.domain.model.Recognisable
import com.ecosense.android.featRecognition.domain.model.RecognisableDetail
import com.ecosense.android.featRecognition.domain.model.SavedRecognisable
import kotlinx.coroutines.flow.Flow

interface RecognitionRepository {
    fun recognise(
        bitmap: Bitmap,
    ): List<Recognisable>

    fun getSavedRecognisables(): Flow<Resource<List<SavedRecognisable>>>

    fun getDisease(
        label: String,
    ): Disease?

    suspend fun saveRecognisable(
        label: String,
        confidencePercent: Int,
    ): Flow<Resource<Int>>
}
package com.ecosense.android.featDiseaseRecognition.domain.repository

import android.graphics.Bitmap
import com.ecosense.android.featDiseaseRecognition.domain.model.RecognisedDisease

interface DiseaseRecognitionRepository {
    fun analyzeDiseases(
        bitmap: Bitmap
    ): List<RecognisedDisease>
}
package com.ecosense.android.featRecognition.presentation.recognition

import com.ecosense.android.featRecognition.domain.model.RecognitionResult

data class RecognitionState(
    val mainDiagnosis: RecognitionResult?,
    val diffDiagnoses: List<RecognitionResult>?
) {
    companion object {
        val defaultValue = RecognitionState(
            mainDiagnosis = null,
            diffDiagnoses = null
        )
    }
}
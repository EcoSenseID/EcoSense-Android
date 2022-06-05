package com.ecosense.android.featRecognition.presentation.recognition

import com.ecosense.android.featRecognition.domain.model.Recognisable

data class RecognitionState(
    val mainDiagnosis: Recognisable?,
    val diffDiagnoses: List<Recognisable>?,
    val isSavingResult: Boolean,
) {
    companion object {
        val defaultValue = RecognitionState(
            mainDiagnosis = null,
            diffDiagnoses = null,
            isSavingResult = false,
        )
    }
}
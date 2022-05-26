package com.ecosense.android.featDiseaseRecognition.presentation

import com.ecosense.android.featDiseaseRecognition.domain.model.RecognisedDisease

data class DiseaseRecognitionState(
    val mainDiagnosis: RecognisedDisease?,
    val diffDiagnoses: List<RecognisedDisease>?
) {
    companion object {
        val defaultValue = DiseaseRecognitionState(
            mainDiagnosis = null,
            diffDiagnoses = null
        )
    }
}
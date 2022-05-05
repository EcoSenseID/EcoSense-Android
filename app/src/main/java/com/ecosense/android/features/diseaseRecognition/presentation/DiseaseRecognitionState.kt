package com.ecosense.android.features.diseaseRecognition.presentation

import com.ecosense.android.features.diseaseRecognition.domain.RecognisedDisease

data class DiseaseRecognitionState(
    val recognisedDiseasesList: List<RecognisedDisease>
) {
    companion object {
        val defaultValue = DiseaseRecognitionState(
            recognisedDiseasesList = emptyList()
        )
    }
}
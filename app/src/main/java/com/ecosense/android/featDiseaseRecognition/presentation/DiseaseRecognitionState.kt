package com.ecosense.android.featDiseaseRecognition.presentation

import com.ecosense.android.featDiseaseRecognition.domain.model.RecognisedDisease

data class DiseaseRecognitionState(
    val recognisedDiseasesList: List<RecognisedDisease>
) {
    companion object {
        val defaultValue = DiseaseRecognitionState(
            recognisedDiseasesList = emptyList()
        )
    }
}
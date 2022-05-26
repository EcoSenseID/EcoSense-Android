package com.ecosense.android.featDiseaseRecognition.domain.model

data class RecognisedDisease(
    val label: String,
    val confidence: Float
)
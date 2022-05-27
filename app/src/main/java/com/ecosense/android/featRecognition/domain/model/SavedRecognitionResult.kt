package com.ecosense.android.featRecognition.domain.model

data class SavedRecognitionResult(
    val id: Int? = null,
    val label: String,
    val confidencePercent: Int,
    val timeInMillis: Long,
)
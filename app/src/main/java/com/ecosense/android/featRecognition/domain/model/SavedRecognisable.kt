package com.ecosense.android.featRecognition.domain.model

data class SavedRecognisable(
    val id: Int?,
    val timeInMillis: Long,
    val label: String,
    val confidencePercent: Int,
    val symptoms: String?,
    val treatment: String?,
    val preventiveMeasure: String?
)
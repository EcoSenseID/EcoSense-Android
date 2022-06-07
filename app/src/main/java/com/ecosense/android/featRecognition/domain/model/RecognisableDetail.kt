package com.ecosense.android.featRecognition.domain.model

data class RecognisableDetail(
    val id: Int?,
    val label: String,
    val savedAt: Int,
    val confidencePercent: Int,
)
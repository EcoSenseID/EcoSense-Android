package com.ecosense.android.featRecognition.domain.model

data class RecognisableDetail(
    val id: Int?,
    val label: String,
    val timeInMillis: Long,
    val confidencePercent: Int,
)
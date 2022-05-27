package com.ecosense.android.featRecognition.domain.model

import com.ecosense.android.core.util.UIText

data class RecognitionResult(
    val label: UIText,
    val confidencePercent: Int
)
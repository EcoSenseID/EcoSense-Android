package com.ecosense.android.featDiseaseRecognition.domain.model

import com.ecosense.android.core.util.UIText

data class RecognisedDisease(
    val label: UIText,
    val confidence: Float
)
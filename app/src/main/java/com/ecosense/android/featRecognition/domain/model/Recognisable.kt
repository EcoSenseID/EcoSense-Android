package com.ecosense.android.featRecognition.domain.model

import com.ecosense.android.core.util.UIText

data class Recognisable(
    val label: String,
    val confidencePercent: Int,
    val readableName: UIText?,
)
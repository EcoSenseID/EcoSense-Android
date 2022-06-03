package com.ecosense.android.featRecognition.domain.model

import com.ecosense.android.core.util.UIText

data class Recognisable(
    val label: UIText,
    val confidencePercent: Int,
    val symptoms: String?,
    val treatments: String?,
    val preventiveMeasures: String?
)
package com.ecosense.android.featRecognition.domain.model

import com.ecosense.android.core.util.UIText

data class Disease(
    val label: String,
    val readableName: UIText?,
    val symptoms: UIText?,
    val treatments: UIText?,
    val preventiveMeasures: UIText?,
)
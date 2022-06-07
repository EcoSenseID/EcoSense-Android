package com.ecosense.android.featRecognition.domain.model

import com.ecosense.android.core.util.UIText

data class SavedRecognisable(
    val id: Int?,
    val label: String,
    val savedAt: Int,
    val readableName: UIText?,
    val confidencePercent: Int,
)
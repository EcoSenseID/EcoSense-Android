package com.ecosense.android.featRecognition.data.model

import com.ecosense.android.core.util.UIText
import com.ecosense.android.featRecognition.domain.model.Disease

data class DiseaseDto(
    val label: String?,
    val preventiveMeasures: String?,
    val readableName: String?,
    val symptoms: String?,
    val treatments: String?,
) {
    fun toDisease() = Disease(
        label = this.label ?: "",
        readableName = UIText.DynamicString(this.readableName ?: this.label ?: ""),
        symptoms = this.symptoms?.let { UIText.DynamicString(it) },
        treatments = this.treatments?.let { UIText.DynamicString(it) },
        preventiveMeasures = this.preventiveMeasures?.let { UIText.DynamicString(it) },
    )
}
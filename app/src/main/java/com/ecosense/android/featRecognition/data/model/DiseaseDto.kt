package com.ecosense.android.featRecognition.data.model

import com.ecosense.android.core.util.UIText
import com.ecosense.android.featRecognition.domain.model.Disease
import com.google.gson.annotations.SerializedName

data class DiseaseDto(
    @SerializedName("label") val label: String?,
    @SerializedName("preventiveMeasures") val preventiveMeasures: String?,
    @SerializedName("readableName") val readableName: String?,
    @SerializedName("symptoms") val symptoms: String?,
    @SerializedName("treatments") val treatments: String?,
) {
    fun toDisease() = Disease(
        label = this.label ?: "",
        readableName = UIText.DynamicString(this.readableName ?: this.label ?: ""),
        symptoms = this.symptoms?.let { UIText.DynamicString(it) },
        treatments = this.treatments?.let { UIText.DynamicString(it) },
        preventiveMeasures = this.preventiveMeasures?.let { UIText.DynamicString(it) },
    )
}
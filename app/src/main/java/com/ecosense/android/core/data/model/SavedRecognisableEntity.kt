package com.ecosense.android.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ecosense.android.featRecognition.domain.model.SavedRecognisable

@Entity
data class SavedRecognisableEntity(
    @PrimaryKey val id: Int? = null,
    val timeInMillis: Long,
    val label: String,
    val confidencePercent: Int,
    val symptoms: String? = null,
    val treatment: String? = null,
    val preventiveMeasure: String? = null
) {
    fun toSavedRecognisable() = SavedRecognisable(
        id = id,
        timeInMillis = timeInMillis,
        label = label,
        confidencePercent = confidencePercent,
        symptoms = symptoms,
        treatment = treatment,
        preventiveMeasure = preventiveMeasure,
    )
}
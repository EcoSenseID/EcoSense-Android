package com.ecosense.android.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ecosense.android.featRecognition.domain.model.SavedRecognitionResult

@Entity
data class SavedRecognitionResultEntity(
    @PrimaryKey val id: Int? = null,
    val label: String,
    val confidencePercent: Int,
    val timeInMillis: Long,
) {
    fun toDiseaseHistoryItem() = SavedRecognitionResult(
        id = id,
        label = label,
        confidencePercent = confidencePercent,
        timeInMillis = timeInMillis,
    )
}
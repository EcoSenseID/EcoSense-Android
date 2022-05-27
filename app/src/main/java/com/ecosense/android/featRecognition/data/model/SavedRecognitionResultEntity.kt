package com.ecosense.android.featRecognition.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ecosense.android.featRecognition.domain.model.SavedRecognitionResult

@Entity
data class SavedRecognitionResultEntity(
    @PrimaryKey val id: Int? = null,
    val label: String,
    val confidence: Float,
    val timeInMillis: Long,
) {
    fun toDiseaseHistoryItem() = SavedRecognitionResult(
        id = id,
        label = label,
        confidence = confidence,
        timeInMillis = timeInMillis,
    )
}
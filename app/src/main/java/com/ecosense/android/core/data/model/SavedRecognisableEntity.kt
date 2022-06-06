package com.ecosense.android.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SavedRecognisableEntity(
    @PrimaryKey val id: Int? = null,
    val timeInMillis: Long,
    val label: String,
    val confidencePercent: Int,
)
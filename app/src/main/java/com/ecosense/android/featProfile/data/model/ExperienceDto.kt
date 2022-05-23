package com.ecosense.android.featProfile.data.model

import com.ecosense.android.featProfile.domain.model.Experience

data class ExperienceDto(
    val categoryName: String?,
    val level: Int?,
    val currentExperience: Int?,
    val levelExperience: Int?
) {
    fun toExperience() = Experience(
        categoryName = categoryName ?: "",
        level = level ?: 0,
        currentExperience = currentExperience ?: 0,
        levelExperience = levelExperience ?: 0
    )
}
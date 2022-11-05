package com.ecosense.android.featProfile.data.model

import com.ecosense.android.core.data.model.StoryDto

data class GetStoriesHistoryDto(
    val error: Boolean?,
    val message: String?,
    val stories: List<StoryDto>?,
)
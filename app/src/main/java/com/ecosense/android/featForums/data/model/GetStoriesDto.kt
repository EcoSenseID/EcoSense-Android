package com.ecosense.android.featForums.data.model

import com.ecosense.android.core.data.model.StoryDto

data class GetStoriesDto(
    val error: Boolean?,
    val message: String?,
    val stories: List<StoryDto>?,
)
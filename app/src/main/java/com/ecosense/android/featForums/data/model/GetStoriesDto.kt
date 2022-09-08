package com.ecosense.android.featForums.data.model

data class GetStoriesDto(
    val error: Boolean,
    val message: String,
    val stories: List<StoryDto>,
)
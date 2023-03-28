package com.ecosense.android.featForums.data.model

import com.ecosense.android.core.data.model.StoryDto
import com.google.gson.annotations.SerializedName

data class GetStoriesDto(
    @SerializedName("error") val error: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("stories") val stories: List<StoryDto>?,
)
package com.ecosense.android.featProfile.data.model

import com.ecosense.android.core.data.model.StoryDto
import com.google.gson.annotations.SerializedName

data class GetStoriesHistoryDto(
    @SerializedName("error") val error: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("stories") val stories: List<StoryDto>?,
)
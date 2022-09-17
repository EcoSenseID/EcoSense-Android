package com.ecosense.android.featForums.data.model

data class GetStoryLikesDto(
    val error: Boolean,
    val likedBy: List<LikedByDto>,
    val message: String
)
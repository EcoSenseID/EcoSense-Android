package com.ecosense.android.featForums.data.model

data class StoryDto(
    val id: Int,
    val username: String,
    val caption: String,
    val createdAt: Int,
    val isLiked: Boolean,
    val likesCount: Int,
    val name: String,
    val photoUrl: String
)
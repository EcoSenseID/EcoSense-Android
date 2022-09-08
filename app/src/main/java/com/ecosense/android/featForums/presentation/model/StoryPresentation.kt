package com.ecosense.android.featForums.presentation.model

data class StoryPresentation(
    val id: Int,
    val name: String,
    val username: String,
    val caption: String,
    val photoUrl: String,
    val createdAt: String,
    val likesCount: Int,
    val commentsCount: Int,
    val isLiked: Boolean,
)
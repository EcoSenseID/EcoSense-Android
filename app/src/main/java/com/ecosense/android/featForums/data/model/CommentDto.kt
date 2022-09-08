package com.ecosense.android.featForums.data.model

data class CommentDto(
    val content: String,
    val createdAt: Int,
    val id: Int,
    val isLiked: Boolean,
    val likesCount: Int,
    val name: String
)
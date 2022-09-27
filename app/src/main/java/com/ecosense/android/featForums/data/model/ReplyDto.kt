package com.ecosense.android.featForums.data.model

data class ReplyDto(
    val id: Int,
    val content: String,
    val createdAt: Int,
    val isLiked: Boolean,
    val likesCount: Int,
    val name: String
)
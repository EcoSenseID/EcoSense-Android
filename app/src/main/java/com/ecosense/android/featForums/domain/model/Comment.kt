package com.ecosense.android.featForums.domain.model

data class Comment(
    val id: Int,
    val name: String,
    val username: String,
    val profilePictureUrl: String,
    val caption: String,
    val photoUrl: String?,
    val createdAt: Long,
    val likesCount: Int,
    val isLiked: Boolean,
)
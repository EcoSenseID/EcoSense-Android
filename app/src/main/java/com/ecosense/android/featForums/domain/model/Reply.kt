package com.ecosense.android.featForums.domain.model

data class Reply(
    val id: Int,
    val name: String,
    val username: String,
    val avatarUrl: String,
    val caption: String,
    val attachedPhotoUrl: String?,
    val createdAt: Long,
    val supportersCount: Int,
    val isSupported: Boolean,
)
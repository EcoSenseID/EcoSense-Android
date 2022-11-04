package com.ecosense.android.featForums.data.model

data class GetRepliesDto(
    val error: Boolean?,
    val message: String?,
    val replies: List<ReplyDto>?,
)
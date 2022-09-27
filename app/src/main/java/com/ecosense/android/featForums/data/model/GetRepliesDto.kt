package com.ecosense.android.featForums.data.model

data class GetRepliesDto(
    val replies: List<ReplyDto>,
    val error: Boolean,
    val message: String
)
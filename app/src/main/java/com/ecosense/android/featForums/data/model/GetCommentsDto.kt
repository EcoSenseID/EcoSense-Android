package com.ecosense.android.featForums.data.model

data class GetCommentsDto(
    val comments: List<CommentDto>,
    val error: Boolean,
    val message: String
)
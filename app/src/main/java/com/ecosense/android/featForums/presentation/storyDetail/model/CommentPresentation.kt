package com.ecosense.android.featForums.presentation.storyDetail.model

import com.ecosense.android.featForums.domain.model.Comment
import com.ecosense.android.featForums.presentation.constants.PatternConstants
import java.text.SimpleDateFormat
import java.util.*

data class CommentPresentation(
    val id: Int,
    val name: String,
    val username: String,
    val profilePictureUrl: String,
    val caption: String,
    val photoUrl: String?,
    val createdAt: String,
    val likesCount: Int,
    val isLiked: Boolean,
)

fun Comment.toPresentation() = CommentPresentation(
    id = this.id,
    name = this.name,
    username = this.username,
    profilePictureUrl = this.profilePictureUrl,
    caption = this.caption,
    photoUrl = this.photoUrl,
    createdAt = SimpleDateFormat(
        PatternConstants.STORIES_DATE_FORMAT,
        Locale.getDefault(),
    ).format(Date().apply { time = this@toPresentation.createdAt }),
    likesCount = this.likesCount,
    isLiked = this.isLiked,
)
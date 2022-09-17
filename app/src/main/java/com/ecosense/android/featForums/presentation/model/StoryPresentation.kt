package com.ecosense.android.featForums.presentation.model

import android.os.Parcelable
import com.ecosense.android.featForums.domain.model.Story
import com.ecosense.android.featForums.presentation.constants.PatternConstants
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class StoryPresentation(
    val id: Int,
    val name: String,
    val username: String,
    val profilePictureUrl: String,
    val caption: String,
    val photoUrl: String?,
    val createdAt: String,
    val likesCount: Int,
    val commentsCount: Int,
    val isLiked: Boolean,
) : Parcelable

fun Story.toPresentation(): StoryPresentation {
    return StoryPresentation(
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
        commentsCount = this.commentsCount,
        isLiked = this.isLiked,
    )
}
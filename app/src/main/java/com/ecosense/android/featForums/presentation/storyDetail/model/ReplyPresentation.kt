package com.ecosense.android.featForums.presentation.storyDetail.model

import com.ecosense.android.featForums.domain.model.Reply
import com.ecosense.android.featForums.presentation.constants.PatternConstants
import java.text.SimpleDateFormat
import java.util.*

data class ReplyPresentation(
    val id: Int,
    val name: String,
    val avatarUrl: String,
    val caption: String,
    val attachedPhotoUrl: String?,
    val createdAt: String,
    val supportersCount: Int,
    val isSupported: Boolean,
)

fun Reply.toPresentation() = ReplyPresentation(
    id = this.id,
    name = this.name,
    avatarUrl = this.avatarUrl,
    caption = this.caption,
    attachedPhotoUrl = this.attachedPhotoUrl,
    createdAt = SimpleDateFormat(
        PatternConstants.STORIES_DATE_FORMAT,
        Locale.getDefault(),
    ).format(Date().apply { time = this@toPresentation.createdAt }),
    supportersCount = this.supportersCount,
    isSupported = this.isSupported,
)
package com.ecosense.android.featForums.presentation.storyDetail.model

import com.ecosense.android.core.presentation.constants.PatternConstants
import com.ecosense.android.featForums.domain.model.Reply
import java.text.SimpleDateFormat
import java.util.*

data class ReplyPresentation(
    val id: Int,
    val name: String,
    val userId: Int,
    val avatarUrl: String,
    val caption: String,
    val attachedPhotoUrl: String?,
    val createdAt: String,
    val supportersCount: Int,
    val isSupported: Boolean,
    val isLoadingSupport: Boolean,
)

fun Reply.toPresentation() = ReplyPresentation(
    id = this.id,
    name = this.name,
    userId = this.userId,
    avatarUrl = this.avatarUrl,
    caption = this.caption,
    attachedPhotoUrl = this.attachedPhotoUrl,
    createdAt = SimpleDateFormat(
        PatternConstants.DEFAULT_DATE_FORMAT,
        Locale.getDefault(),
    ).format(Date().apply { time = this@toPresentation.createdAt }),
    supportersCount = this.supportersCount,
    isSupported = this.isSupported,
    isLoadingSupport = false,
)
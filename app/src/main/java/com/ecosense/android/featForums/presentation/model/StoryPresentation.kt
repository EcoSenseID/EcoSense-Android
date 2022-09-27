package com.ecosense.android.featForums.presentation.model

import android.os.Parcelable
import com.ecosense.android.core.presentation.model.CampaignPresentation
import com.ecosense.android.core.presentation.model.toPresentation
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
    val avatarUrl: String,
    val caption: String,
    val attachedPhotoUrl: String?,
    val sharedCampaign: CampaignPresentation?,
    val createdAt: String,
    val supportersCount: Int,
    val supportersAvatarsUrl: List<String>,
    val repliesCount: Int,
    val isSupported: Boolean,
) : Parcelable

fun Story.toPresentation(): StoryPresentation {
    return StoryPresentation(
        id = this.id,
        name = this.name,
        username = this.username,
        avatarUrl = this.avatarUrl,
        caption = this.caption,
        attachedPhotoUrl = this.attachedPhotoUrl,
        sharedCampaign = this.sharedCampaign?.toPresentation(),
        createdAt = SimpleDateFormat(
            PatternConstants.STORIES_DATE_FORMAT,
            Locale.getDefault(),
        ).format(Date().apply { time = this@toPresentation.createdAt }),
        supportersCount = this.supportersCount,
        repliesCount = this.repliesCount,
        isSupported = this.isSupported,
        supportersAvatarsUrl = this.supportersAvatarsUrl,
    )
}
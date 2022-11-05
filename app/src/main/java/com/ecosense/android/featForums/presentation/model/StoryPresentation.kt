package com.ecosense.android.featForums.presentation.model

import android.os.Parcelable
import com.ecosense.android.core.domain.model.Story
import com.ecosense.android.core.presentation.constants.PatternConstants
import com.ecosense.android.core.presentation.model.SharedCampaignPresentation
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class StoryPresentation(
    val id: Int,
    val name: String,
    val avatarUrl: String,
    val caption: String,
    val attachedPhotoUrl: String?,
    val sharedCampaign: SharedCampaignPresentation?,
    val createdAt: String,
    val supportersCount: Int,
    val supportersAvatarsUrl: List<String>,
    val repliesCount: Int,
    val isSupported: Boolean,
    val isLoadingSupport: Boolean,
) : Parcelable {
    companion object {
        val defaultValue = StoryPresentation(
            id = 0,
            name = "",
            avatarUrl = "",
            caption = "",
            attachedPhotoUrl = "",
            sharedCampaign = null,
            createdAt = "",
            supportersCount = 0,
            supportersAvatarsUrl = emptyList(),
            repliesCount = 0,
            isSupported = false,
            isLoadingSupport = false,
        )
    }
}

fun Story.toPresentation(): StoryPresentation {
    return StoryPresentation(
        id = this.id,
        name = this.name,
        avatarUrl = this.avatarUrl,
        caption = this.caption,
        attachedPhotoUrl = this.attachedPhotoUrl,
        sharedCampaign = this.sharedCampaign?.toPresentation(),
        createdAt = SimpleDateFormat(
            PatternConstants.DEFAULT_DATE_FORMAT,
            Locale.getDefault(),
        ).format(Date().apply { time = this@toPresentation.createdAt }),
        supportersCount = this.supportersCount,
        repliesCount = this.repliesCount,
        isSupported = this.isSupported,
        supportersAvatarsUrl = this.supportersAvatarsUrl,
        isLoadingSupport = false,
    )
}
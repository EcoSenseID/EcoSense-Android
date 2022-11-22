package com.ecosense.android.featProfile.presentation.model

import com.ecosense.android.core.presentation.constants.PatternConstants
import com.ecosense.android.core.presentation.model.CategoryPresentation
import com.ecosense.android.core.presentation.model.toPresentation
import com.ecosense.android.featProfile.domain.model.RecentCampaign
import java.text.SimpleDateFormat
import java.util.*

data class RecentCampaignPresentation(
    val id: Int,
    val recordId: Int?,
    val posterUrl: String,
    val title: String,
    val earnedPoints: Int,
    val finishedAt: String,
    val endAt: String,
    val completionStatus: Int,
    val categories: List<CategoryPresentation>
)

fun RecentCampaign.toPresentation() = RecentCampaignPresentation(
    id = this.id,
    recordId = this.recordId,
    posterUrl = this.posterUrl,
    title = this.title,
    earnedPoints = this.earnedPoints,
    finishedAt = SimpleDateFormat(
        PatternConstants.DEFAULT_DATE_FORMAT,
        Locale.getDefault(),
    ).format(Date().apply { time = this@toPresentation.finishedAt }),
    endAt = SimpleDateFormat(
        PatternConstants.DEFAULT_DATE_FORMAT,
        Locale.getDefault(),
    ).format(Date().apply { time = this@toPresentation.endAt }),
    completionStatus = this.completionStatus,
    categories = categories.map { it.toPresentation() },
)
package com.ecosense.android.featProfile.presentation.profile.model

import com.ecosense.android.featForums.presentation.constants.PatternConstants
import com.ecosense.android.featProfile.domain.model.FinishedCampaign
import java.text.SimpleDateFormat
import java.util.*

data class FinishedCampaignPresentation(
    val title: String,
    val posterUrl: String,
    val completedAt: String,
    val categories: List<String>,
)

fun FinishedCampaign.toPresentation() = FinishedCampaignPresentation(
    title = this.title,
    posterUrl = this.posterUrl,
    completedAt = SimpleDateFormat(
        PatternConstants.STORIES_DATE_FORMAT,
        Locale.getDefault(),
    ).format(Date().apply { time = this@toPresentation.completedAt }),
    categories = this.categories,
)
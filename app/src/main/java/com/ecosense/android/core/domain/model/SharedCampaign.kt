package com.ecosense.android.core.domain.model

import com.ecosense.android.core.presentation.constants.PatternConstants
import com.ecosense.android.core.presentation.model.SharedCampaignPresentation
import com.ecosense.android.core.presentation.model.toPresentation
import java.text.SimpleDateFormat
import java.util.*

data class SharedCampaign(
    val id: Int,
    val posterUrl: String,
    val title: String,
    val endAt: Long,
    val categories: List<Category>,
    val participantsCount: Int,
    val isTrending: Boolean,
    val isNew: Boolean,
) {
    fun toPresentation(): SharedCampaignPresentation = SharedCampaignPresentation(
        id = this.id,
        posterUrl = this.posterUrl,
        title = this.title,
        endAt = SimpleDateFormat(
            PatternConstants.DEFAULT_DATE_FORMAT,
            Locale.getDefault(),
        ).format(Date().apply { time = this@SharedCampaign.endAt }),
        categories = this.categories.map { it.toPresentation() },
        participantsCount = this.participantsCount,
        isTrending = this.isTrending,
        isNew = this.isNew,
    )
}
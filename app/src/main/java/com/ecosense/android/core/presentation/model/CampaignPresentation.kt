package com.ecosense.android.core.presentation.model

import android.os.Parcelable
import com.ecosense.android.core.domain.model.Campaign
import kotlinx.parcelize.Parcelize

@Parcelize
data class CampaignPresentation(
    val id: Int,
    val posterUrl: String,
    val title: String,
    val endDate: String,
    val categories: List<String>,
    val participantsCount: Int,
    val isTrending: Boolean,
    val isNew: Boolean
) : Parcelable

fun Campaign.toPresentation() = CampaignPresentation(
    id = this.id,
    posterUrl = this.posterUrl,
    title = this.title,
    endDate = this.endDate,
    categories = this.category,
    participantsCount = this.participantsCount,
    isTrending = this.isTrending,
    isNew = this.isNew,
)
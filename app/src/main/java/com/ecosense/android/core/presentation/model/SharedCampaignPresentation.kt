package com.ecosense.android.core.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SharedCampaignPresentation(
    val id: Int,
    val posterUrl: String,
    val title: String,
    val endAt: String,
    val categories: List<CategoryPresentation>,
    val participantsCount: Int,
    val isTrending: Boolean,
    val isNew: Boolean,
) : Parcelable
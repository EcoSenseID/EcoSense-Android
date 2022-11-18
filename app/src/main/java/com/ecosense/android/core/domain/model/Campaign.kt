package com.ecosense.android.core.domain.model

import com.ecosense.android.featDiscoverCampaign.domain.model.BrowseCategory

data class Campaign(
    val id: Int,
    val posterUrl: String,
    val title: String,
    val endDate: String,
    val startDate: String,
    val categories: List<BrowseCategory>,
    val participantsCount: Int,
    val isTrending: Boolean,
    val isNew: Boolean
)
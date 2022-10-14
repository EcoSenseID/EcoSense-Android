package com.ecosense.android.featProfile.domain.model

import com.ecosense.android.core.domain.model.Story

data class Profile(
    val totalEcoPoints: Int,
    val finishedCampaigns: List<FinishedCampaign>,
    val postedStories: List<Story>
)
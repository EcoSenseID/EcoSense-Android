package com.ecosense.android.featProfile.domain.model

import com.ecosense.android.core.domain.model.Campaign

data class Contributions(
    val experiences: List<Experience>,
    val completedCampaigns: List<Campaign>,
) {
    companion object {
        val defaultValue = Contributions(
            experiences = emptyList(),
            completedCampaigns = emptyList(),
        )
    }
}
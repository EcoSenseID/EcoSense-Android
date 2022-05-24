package com.ecosense.android.featDiscoverCampaign.data.util

import com.ecosense.android.core.domain.model.Campaign

object Faker {
    fun getBrowseCampaign(): List<Campaign> {
        val result = mutableListOf<Campaign>()

        for (i in 1..50) {
            result.add(
                Campaign(
                    id = i,
                    posterUrl = "https://cdn.statically.io/og/theme=dark/Campaign$i.jpg",
                    title = "Lorem Ipsum $i",
                    description = "Attach the proof by submitting $i photo",
                    startDate = "2022-04-${i}T00:00:00.000Z",
                    endDate = "2022-05-${i}T00:00:00.000Z",
                    category = listOf("Dolor $i"),
                    participantsCount = (1000..5000).random(),
                    isTrending = i < 3,
                    isNew = i < 5,
                    isJoined = i < 10
                )
            )
        }

        return result
    }
}
package com.ecosense.android.featDiscoverCampaign.data.util

import com.ecosense.android.featDiscoverCampaign.domain.model.Campaign

object Faker {
    fun getBrowseCampaign(): List<Campaign> {
        val result = mutableListOf<Campaign>()

        for (i in 1..50) {
            result.add(
                Campaign(
                    posterUrl = "https://cdn.statically.io/og/poster_$i.jpg",
                    title = "Lorem Ipsum $i",
                    endDate = "2022-05-${i}T00:00:00.000Z",
                    category = "Dolor $i",
                    participantsCount = (1000..5000).random(),
                    isTrending = i < 3,
                    isNew = i < 5
                )
            )
        }

        return result
    }
}
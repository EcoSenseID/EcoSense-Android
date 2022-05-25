package com.ecosense.android.featDiscoverCampaign.data.util

import com.ecosense.android.core.domain.model.Campaign
import com.ecosense.android.featDiscoverCampaign.domain.model.CampaignDetail
import com.ecosense.android.featDiscoverCampaign.domain.model.Category
import com.ecosense.android.featDiscoverCampaign.domain.model.Task

object Faker {
    fun getBrowseCampaign(): List<Campaign> {
        val result = mutableListOf<Campaign>()

        for (i in 1..50) {
            result.add(
                Campaign(
                    id = i,
                    posterUrl = "https://cdn.statically.io/og/theme=dark/Campaign$i.jpg",
                    title = "Lorem Ipsum $i",
                    endDate = "2022-10-2${i%2}T00:00:00.000Z",
                    category = listOf("#Dolor$i"),
                    participantsCount = (1000..5000).random(),
                    isTrending = i < 3,
                    isNew = i < 5
                )
            )
        }

        return result
    }

    fun getCampaignDetail(): List<CampaignDetail> {
        val result = mutableListOf<CampaignDetail>()

        for (i in 1..50) {
            result.add(
                CampaignDetail(
                    id = i,
                    posterUrl = "https://cdn.statically.io/og/theme=dark/Campaign$i.jpg",
                    initiator = "Olaf Number $i",
                    title = "Lorem Ipsum $i",
                    description = "This $i campaign aims to reduce food waste that is categorized as a catalyst in increasing global warming. This is due to the increase of food industries that have some oogabooga workers.",
                    startDate = "2022-09-2${i%2}T00:00:00.000Z",
                    endDate = "2022-10-2${i%2}T00:00:00.000Z",
                    category = listOf("#Dolor$i", "#AirPollution", "#FoodWaste"),
                    participantsCount = (1000..5000).random(),
                    isTrending = i < 3,
                    isNew = i < 5,
                    isJoined = i < 10,
                    tasks = getTask()
                )
            )
        }

        return result
    }

    fun getCategory(): List<Category> {
        val result = mutableListOf<Category>()

        for (i in 1..8) {
            result.add(
                Category(
                    id = i,
                    photoUrl = "https://cdn.statically.io/og/theme=dark/Category$i.jpg",
                    name = "Category No.$i"
                )
            )
        }

        return result
    }

    private fun getTask(): List<Task> {
        val result = mutableListOf<Task>()

        for (i in 1..10) {
            result.add(
                Task(
                    id = i,
                    name = "Task No.$i",
                    taskDescription = "Attach the proof by submitting a photo of $i",
                    completed = i < 4,
                    proofPhotoUrl = "https://cdn.statically.io/og/theme=dark/TaskProof$i.jpg",
                    proofCaption = "I've done the $i task",
                    completedTimeStamp = "2022-04-${i}T00:00:00.000Z",
                )
            )
        }

        return result
    }
}
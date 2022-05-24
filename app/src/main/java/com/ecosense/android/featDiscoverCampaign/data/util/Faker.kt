package com.ecosense.android.featDiscoverCampaign.data.util

import com.ecosense.android.core.domain.model.Campaign
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

    fun getCategory(): List<Category> {
        val result = mutableListOf<Category>()

        for (i in 1..8) {
            result.add(
                Category(
                    id = i,
                    photoUrl = "https://cdn.statically.io/og/theme=dark/Category$i.jpg",
                    name = "Category $i"
                )
            )
        }

        return result
    }

    fun getTask(): List<Task> {
        val result = mutableListOf<Task>()

        for (i in 1..10) {
            result.add(
                Task(
                    id = i,
                    name = "Task $i",
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
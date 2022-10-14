package com.ecosense.android.featProfile.data.api

import com.ecosense.android.featProfile.data.model.FinishedCampaignDto
import com.ecosense.android.featProfile.data.model.PostedStoryDto
import com.ecosense.android.featProfile.data.model.ProfileDto
import com.ecosense.android.featProfile.data.model.SharedCampaignDto
import kotlinx.coroutines.delay

class FakeProfileApi : ProfileApi {
    override suspend fun getProfile(bearerToken: String): ProfileDto {
        delay(1500)
        return ProfileDto(
            finishedCampaigns = listOf(
                FinishedCampaignDto(
                    categories = listOf("Air Pollution", "Food Waste"),
                    completedAt = 1665645721,
                    id = 1,
                    posterUrl = "https://cdn.statically.io/og/theme=dark/food_waste_1.jpg",
                    title = "No More Food Waste: Hassle-Free Compost",
                ),
                FinishedCampaignDto(
                    categories = listOf("Air Pollution", "Food Waste"),
                    completedAt = 1665645721,
                    id = 1,
                    posterUrl = "https://cdn.statically.io/og/theme=dark/food_waste_2.jpg",
                    title = "No More Food Waste: Hassle-Free Compost",
                )
            ),
            error = false,
            message = "Profile fetched successfully",
            postedStories = listOf(
                PostedStoryDto(
                    id = 1,
                    name = "John Doe",
                    avatarUrl = "https://i.pravatar.cc/300?img=$1",
                    caption = "Bagaimana caramu untuk mengajak masyarakat melestarikan lingkungan?",
                    attachedPhotoUrl = "https://cdn.statically.io/og/theme=dark/Story%20no.%201.jpg",
                    sharedCampaign = SharedCampaignDto(
                        id = 1,
                        posterUrl = "https://cdn.statically.io/og/theme=dark/shared_campaign_1.jpg",
                        title = "Shared Campaign 1",
                        endDate = "24 July 2022",
                        category = listOf("Water Pollution", "Plastic Free"),
                        participantsCount = 69420,
                        isTrending = true,
                        isNew = true,
                    ),
                    createdAt = System.currentTimeMillis(),
                    supportersCount = 420,
                    repliesCount = 69,
                    isSupported = true,
                    supportersAvatarsUrl = listOf(
                        "https://i.pravatar.cc/300?img=2",
                        "https://i.pravatar.cc/300?img=3",
                        "https://i.pravatar.cc/300?img=4",
                    ),
                ),
                PostedStoryDto(
                    id = 1,
                    name = "John Doe",
                    avatarUrl = "https://i.pravatar.cc/300?img=$1",
                    caption = "Bagaimana caramu untuk mengajak masyarakat melestarikan lingkungan?",
                    attachedPhotoUrl = "https://cdn.statically.io/og/theme=dark/Story%20no.%201.jpg",
                    sharedCampaign = null,
                    createdAt = System.currentTimeMillis(),
                    supportersCount = 420,
                    repliesCount = 69,
                    isSupported = true,
                    supportersAvatarsUrl = listOf(
                        "https://i.pravatar.cc/300?img=2",
                        "https://i.pravatar.cc/300?img=3",
                        "https://i.pravatar.cc/300?img=4",
                    ),
                )
            ),
            totalEcoPoints = 2700,
        )
    }
}
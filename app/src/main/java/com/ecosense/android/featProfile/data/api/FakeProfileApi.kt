package com.ecosense.android.featProfile.data.api

import com.ecosense.android.core.data.model.CategoryDto
import com.ecosense.android.core.data.model.SharedCampaignDto
import com.ecosense.android.core.data.model.StoryDto
import com.ecosense.android.featProfile.data.model.*
import kotlinx.coroutines.delay

class FakeProfileApi : ProfileApi {
    override suspend fun getProfile(bearerToken: String): ProfileDto {
        delay(500)
        return ProfileDto(
            error = false,
            message = "Profile fetched successfully",
            totalEcoPoints = 2700,
            recentStories = listOf(
                StoryDto(
                    id = 1,
                    name = "John Doe",
                    avatarUrl = "https://i.pravatar.cc/300?img=$1",
                    caption = "Bagaimana caramu untuk mengajak masyarakat melestarikan lingkungan?",
                    attachedPhotoUrl = "https://cdn.statically.io/og/theme=dark/Story%20no.%201.jpg",
                    sharedCampaign = SharedCampaignDto(
                        id = 1,
                        posterUrl = "https://cdn.statically.io/og/theme=dark/shared_campaign_1.jpg",
                        title = "Shared Campaign 1",
                        endAt = System.currentTimeMillis(),
                        categories = listOf(
                            CategoryDto(name = "Air Pollution", colorHex = "#BADA55"),
                            CategoryDto(name = "Food Waste", colorHex = "#FA6607"),
                        ),
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
                StoryDto(
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
            recentCampaigns = listOf(
                RecentCampaignDto(
                    id = 1,
                    posterUrl = "https://cdn.statically.io/og/theme=dark/food_waste_1.jpg",
                    title = "No More Food Waste: Hassle-Free Compost",
                    earnedPoints = 300,
                    finishedAt = null,
                    endAt = System.currentTimeMillis(),
                    completionStatus = 1,
                    categories = listOf(
                        CategoryDto(name = "Air Pollution", colorHex = "#BADA55"),
                        CategoryDto(name = "Food Waste", colorHex = "#FA6607"),
                    ),
                ),
                RecentCampaignDto(
                    id = 1,
                    posterUrl = "https://cdn.statically.io/og/theme=dark/food_waste_1.jpg",
                    title = "No More Food Waste: Hassle-Free Compost",
                    earnedPoints = 300,
                    finishedAt = 1665645721,
                    endAt = System.currentTimeMillis(),
                    completionStatus = 2,
                    categories = listOf(
                        CategoryDto(name = "Air Pollution", colorHex = "#BADA55"),
                        CategoryDto(name = "Food Waste", colorHex = "#FA6607"),
                    ),
                ),
                RecentCampaignDto(
                    id = 1,
                    posterUrl = "https://cdn.statically.io/og/theme=dark/food_waste_1.jpg",
                    title = "No More Food Waste: Hassle-Free Compost",
                    earnedPoints = 300,
                    finishedAt = 1665645721,
                    endAt = System.currentTimeMillis(),
                    completionStatus = 3,
                    categories = listOf(
                        CategoryDto(name = "Air Pollution", colorHex = "#BADA55"),
                        CategoryDto(name = "Food Waste", colorHex = "#FA6607"),
                    ),
                ),
            ),
        )
    }
}
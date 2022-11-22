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
                    userId = 1,
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
                ), StoryDto(
                    id = 1,
                    userId = 1,
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
                    recordId = 1,
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
                    recordId = 1,
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
                    recordId = 1,
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

    override suspend fun getOthersProfile(
        bearerToken: String,
        userId: Int,
    ): OthersProfileDto {
        delay(500)
        return OthersProfileDto(
            error = false,
            message = "Profile fetched successfully",
            userId = 1,
            name = "John Doe",
            avatarUrl = "https://i.pravatar.cc/300?img=$1",
            recentStories = listOf(
                StoryDto(
                    id = 1,
                    userId = 1,
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
                ), StoryDto(
                    id = 1,
                    userId = 1,
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
                    recordId = 1,
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
                    recordId = 1,
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
                    recordId = 1,
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

    override suspend fun getStoriesHistory(
        bearerToken: String,
        userId: Int?
    ): GetStoriesHistoryDto {
        return GetStoriesHistoryDto(
            error = false,
            message = null,
            stories = (1..100).map {
                StoryDto(
                    id = it,
                    userId = it,
                    name = "John Doe$it",
                    avatarUrl = "https://i.pravatar.cc/300?img=$it",
                    caption = "Bagaimana cara lestarikan lingkungan? ${"lorem ".repeat((it * 69) % 15)}",
                    attachedPhotoUrl = if (it % 3 == 0) "https://cdn.statically.io/og/theme=dark/Story%20$it.jpg" else null,
                    sharedCampaign = if (it % 7 == 0) SharedCampaignDto(
                        id = 1,
                        posterUrl = "https://cdn.statically.io/og/theme=dark/shared_campaign_$it.jpg",
                        title = "Shared Campaign $it",
                        endAt = System.currentTimeMillis(),
                        categories = listOf(
                            CategoryDto(name = "Air Pollution", colorHex = "#BADA55"),
                            CategoryDto(name = "Food Waste", colorHex = "#FA6607"),
                        ),
                        participantsCount = 69420,
                        isTrending = true,
                        isNew = true,
                    ) else null,
                    createdAt = System.currentTimeMillis() - (it * 90000000),
                    supportersCount = (it * 420) % 69,
                    repliesCount = (it * 69) % 15,
                    isSupported = it % 11 == 0,
                    supportersAvatarsUrl = if (it % 5 != 0) listOf(
                        "https://i.pravatar.cc/300?img=${it + 1}",
                        "https://i.pravatar.cc/300?img=${it + 2}",
                        "https://i.pravatar.cc/300?img=${it + 3}",
                    ) else emptyList()
                )
            },
        )
    }

    override suspend fun getCampaignsHistory(
        bearerToken: String,
        userId: Int?
    ): GetCampaignsHistoryDto {
        return GetCampaignsHistoryDto(
            error = false,
            message = "success",
            campaigns = listOf(
                RecentCampaignDto(
                    id = 1,
                    recordId = 1,
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
                    recordId = 1,
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
                    recordId = 1,
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
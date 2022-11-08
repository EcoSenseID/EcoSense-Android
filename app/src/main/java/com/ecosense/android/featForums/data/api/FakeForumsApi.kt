package com.ecosense.android.featForums.data.api

import com.ecosense.android.core.data.model.CategoryDto
import com.ecosense.android.core.data.model.SharedCampaignDto
import com.ecosense.android.core.data.model.StoryDto
import com.ecosense.android.featForums.data.model.*
import kotlinx.coroutines.delay
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Header
import retrofit2.http.Part

class FakeForumsApi : ForumsApi {

    private val storyDtos = (1..100).map {
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
    }

    private val replyDtos = (1..100).map {
        ReplyDto(
            id = it,
            userId = it,
            name = "Ken Harrington$it",
            avatarUrl = "https://i.pravatar.cc/300?img=$it",
            caption = "Lorem ipsum dolor sit amet $it",
            attachedPhotoUrl = if (it % 3 == 0) "https://cdn.statically.io/og/theme=dark/Reply$it.jpg" else null,
            createdAt = System.currentTimeMillis() - (it * 90000000),
            supportersCount = (it * 420) % 69,
            isSupported = it % 11 == 0,
        )
    }

    private val supporterDtos = (1..100).map {
        SupporterDto(
            id = it,
            userId = it,
            avatarUrl = "https://i.pravatar.cc/300?img=$it",
            name = "Bambang Yudho($it)",
        )
    }

    override suspend fun getStories(
        bearerToken: String,
        page: Int,
        size: Int,
    ): GetStoriesDto {
        delay(500L)

        val startingIndex = page * size

        val stories = if (startingIndex + size <= storyDtos.size) {
            storyDtos.slice(startingIndex until (startingIndex + size))
        } else emptyList()

        return GetStoriesDto(
            error = false,
            message = "Success",
            stories = stories,
        )
    }

    override suspend fun getStoryDetail(bearerToken: String, storyId: Int): GetStoryDetailDto {
        return GetStoryDetailDto(
            attachedPhotoUrl = null,
            avatarUrl = null,
            caption = null,
            createdAt = null,
            error = null,
            id = null,
            userId = null,
            isSupported = null,
            message = null,
            name = null,
            repliesCount = null,
            sharedCampaign = null,
            supportersAvatarsUrl = null,
            supportersCount = null,
        )
    }

    override suspend fun getStoryReplies(
        bearerToken: String,
        storyId: Int,
        page: Int,
        size: Int,
    ): GetRepliesDto {
        delay(500L)

        val startingIndex = page * size

        val replies = if (startingIndex + size <= replyDtos.size) {
            replyDtos.slice(startingIndex until (startingIndex + size))
        } else emptyList()

        return GetRepliesDto(
            error = false,
            message = "Success",
            replies = replies,
        )
    }

    override suspend fun getStorySupporters(
        bearerToken: String,
        storyId: Int,
        page: Int,
        size: Int,
    ): GetStorySupportersDto {
        delay(500L)

        val startingIndex = page * size

        val supporters = if (startingIndex + size <= supporterDtos.size) {
            supporterDtos.slice(startingIndex until (startingIndex + size))
        } else emptyList()

        return GetStorySupportersDto(
            error = false,
            message = "Success",
            supporters = supporters,
        )
    }

    override suspend fun postNewStory(
        @Header(value = "Authorization") bearerToken: String,
        @Part caption: RequestBody,
        @Part sharedCampaignId: RequestBody?,
        @Part attachedPhoto: MultipartBody.Part?
    ): PostNewStoryDto {
        delay(500L)
        return PostNewStoryDto(
            error = false,
            message = "Success",
        )
    }

    override suspend fun postNewReply(
        @Header(value = "Authorization") bearerToken: String,
        @Part storyId: RequestBody,
        @Part caption: RequestBody,
        @Part attachedPhoto: MultipartBody.Part?
    ): PostNewReplyDto {
        delay(500L)
        return PostNewReplyDto(
            error = false,
            message = "Success",
        )
    }

    override suspend fun postSupportStory(
        bearerToken: String,
        storyId: Int,
    ): PostSupportStoryDto {
        delay(500L)
        return PostSupportStoryDto(
            error = false,
            message = "Success",
        )
    }

    override suspend fun postUnsupportStory(
        bearerToken: String, storyId: Int
    ): PostUnsupportStoryDto {
        delay(500L)
        return PostUnsupportStoryDto(
            error = false,
            message = "Success",
        )
    }

    override suspend fun postSupportReply(
        bearerToken: String,
        replyId: Int,
    ): PostSupportReplyDto {
        delay(500L)
        return PostSupportReplyDto(
            error = false,
            message = "Success",
        )
    }

    override suspend fun postUnsupportReply(
        bearerToken: String, replyId: Int
    ): PostUnsupportReplyDto {
        delay(500L)
        return PostUnsupportReplyDto(
            error = false,
            message = "Success",
        )
    }
}
package com.ecosense.android.featForums.data.repository

import com.ecosense.android.core.domain.api.AuthApi
import com.ecosense.android.core.domain.model.Campaign
import com.ecosense.android.core.util.Resource
import com.ecosense.android.featForums.data.api.ForumsApi
import com.ecosense.android.featForums.domain.model.Reply
import com.ecosense.android.featForums.domain.model.Story
import com.ecosense.android.featForums.domain.model.Supporter
import com.ecosense.android.featForums.domain.repository.ForumsRepository
import kotlinx.coroutines.delay

class ForumsRepositoryImpl(
    private val authApi: AuthApi,
    private val forumsApi: ForumsApi,
) : ForumsRepository {

    // TODO: change to fake response instead of domain model list
    private val fakeStories = (1..100).map {
        Story(
            id = it,
            name = "John Doe ($it)",
            username = "@johndoe$it",
            avatarUrl = "https://i.pravatar.cc/300?img=$it",
            caption = "Bagaimana caramu untuk mengajak masyarakat melestarikan lingkungan? ${
                "lorem ipsum ".repeat(
                    (it * 69) % 15
                )
            }",
            attachedPhotoUrl = if (it % 3 == 0) "https://cdn.statically.io/og/theme=dark/Story%20no.%20$it.jpg" else null,
            sharedCampaign = if (it % 7 == 0) Campaign(
                id = 1,
                posterUrl = "https://cdn.statically.io/og/theme=dark/shared_campaign_$it.jpg",
                title = "Shared Campaign $it",
                endDate = "24 July 2022",
                category = listOf("Water Pollution", "Plastic Free"),
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

    private val fakeReplies = (1..100).map {
        Reply(
            id = it,
            name = "Siti ($it)",
            username = "@siti$it",
            avatarUrl = "https://i.pravatar.cc/300?img=$it",
            caption = "Comment caption $it",
            attachedPhotoUrl = if (it % 3 == 0) "https://cdn.statically.io/og/theme=dark/Story$it.jpg" else null,
            createdAt = System.currentTimeMillis() - (it * 90000000),
            supportersCount = (it * 420) % 69,
            isSupported = it % 11 == 0,
        )
    }

    private val fakeSupporters = (1..100).map {
        Supporter(
            id = it,
            avatarUrl = "https://i.pravatar.cc/300?img=$it",
            username = "@siti$it",
            name = "Siti ($it)",
        )
    }

    override suspend fun getStories(
        page: Int,
        size: Int,
    ): Resource<List<Story>> {
        delay(2000L)
        val startingIndex = page * size
        return if (startingIndex + size <= fakeStories.size) {
            Resource.Success(fakeStories.slice(startingIndex until (startingIndex + size)))
        } else Resource.Success(emptyList())
    }

    override suspend fun getComments(
        storyId: Int,
        page: Int,
        size: Int,
    ): Resource<List<Reply>> {
        delay(2000L)
        val startingIndex = page * size
        return if (startingIndex + size <= fakeReplies.size) {
            Resource.Success(fakeReplies.slice(startingIndex until (startingIndex + size)))
        } else Resource.Success(emptyList())
    }

    override suspend fun getSupporters(
        storyId: Int, page: Int, size: Int
    ): Resource<List<Supporter>> {
        delay(2000L)
        val startingIndex = page * size
        return if (startingIndex + size <= fakeSupporters.size) {
            Resource.Success(fakeSupporters.slice(startingIndex until (startingIndex + size)))
        } else Resource.Success(emptyList())
    }
}
package com.ecosense.android.featForums.data.repository

import com.ecosense.android.core.domain.api.AuthApi
import com.ecosense.android.core.util.Resource
import com.ecosense.android.featForums.data.api.ForumsApi
import com.ecosense.android.featForums.domain.model.Comment
import com.ecosense.android.featForums.domain.model.Story
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

    private val fakeComments = (1..100).map {
        Comment(
            id = it,
            name = "Siti ($it)",
            username = "@siti$it",
            profilePictureUrl = "https://cdn.statically.io/og/theme=dark/$it.jpg",
            caption = "Comment caption $it",
            photoUrl = if (it % 3 == 0) "https://cdn.statically.io/og/theme=dark/Story$it.jpg" else null,
            createdAt = System.currentTimeMillis() - (it * 90000000),
            likesCount = (it * 420) % 69,
            isLiked = it % 11 == 0,
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
    ): Resource<List<Comment>> {
        delay(2000L)
        val startingIndex = page * size
        return if (startingIndex + size <= fakeComments.size) {
            Resource.Success(fakeComments.slice(startingIndex until (startingIndex + size)))
        } else Resource.Success(emptyList())
    }
}
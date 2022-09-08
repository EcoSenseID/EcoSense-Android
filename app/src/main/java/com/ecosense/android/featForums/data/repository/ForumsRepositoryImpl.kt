package com.ecosense.android.featForums.data.repository

import com.ecosense.android.core.domain.api.AuthApi
import com.ecosense.android.core.util.Resource
import com.ecosense.android.featForums.data.api.ForumsApi
import com.ecosense.android.featForums.domain.model.Story
import com.ecosense.android.featForums.domain.repository.ForumsRepository
import kotlinx.coroutines.delay

class ForumsRepositoryImpl(
    private val authApi: AuthApi,
    private val forumsApi: ForumsApi,
) : ForumsRepository {

    private val fakeDataSource = (1..100).map {
        Story(
            id = it,
            name = "John Doe ($it)",
            username = "johndoe$it",
            profilePictureUrl = "https://cdn.statically.io/og/$it.jpg",
            caption = "lorem caption $it",
            photoUrl = if (it % 3 == 0) "https://cdn.statically.io/og/Story$it.jpg" else null,
            createdAt = 1662624497 - (it * 1000L),
            likesCount = (it * 420) % 69,
            commentsCount = (it * 69) % 15,
            isLiked = it % 11 == 0,
        )
    }

    override suspend fun getStories(
        page: Int,
        size: Int,
    ): Resource<List<Story>> {
        delay(2000L)
        val startingIndex = page * size
        return if (startingIndex + size <= fakeDataSource.size) {
            Resource.Success(fakeDataSource.slice(startingIndex until (startingIndex + size)))
        } else Resource.Success(emptyList())
    }
}
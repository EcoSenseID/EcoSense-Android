package com.ecosense.android.featForums.domain.repository

import com.ecosense.android.core.util.Resource
import com.ecosense.android.featForums.domain.model.Reply
import com.ecosense.android.core.domain.model.Story
import com.ecosense.android.featForums.domain.model.Supporter

interface ForumsRepository {
    suspend fun getStories(
        page: Int,
        size: Int,
    ): Resource<List<Story>>

    suspend fun getComments(
        storyId: Int,
        page: Int,
        size: Int,
    ): Resource<List<Reply>>

    suspend fun getSupporters(
        storyId: Int,
        page: Int,
        size: Int,
    ): Resource<List<Supporter>>
}
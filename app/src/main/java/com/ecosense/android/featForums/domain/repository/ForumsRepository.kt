package com.ecosense.android.featForums.domain.repository

import com.ecosense.android.core.domain.model.Story
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.SimpleResource
import com.ecosense.android.featForums.domain.model.Reply
import com.ecosense.android.featForums.domain.model.Supporter
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ForumsRepository {
    suspend fun getStories(
        page: Int,
        size: Int,
    ): Resource<List<Story>>

    suspend fun getStoryReplies(
        storyId: Int,
        page: Int,
        size: Int,
    ): Resource<List<Reply>>

    suspend fun getStorySupporters(
        storyId: Int,
        page: Int,
        size: Int,
    ): Resource<List<Supporter>>

    fun postNewStory(
        caption: String,
        attachedPhoto: File? = null,
        sharedCampaignId: Int? = null,
    ): Flow<SimpleResource>

    fun postNewReply(
        storyId: Int,
        caption: String,
        attachedPhoto: File?,
    ): Flow<SimpleResource>

    fun postSupportStory(
        storyId: Int,
    ): Flow<SimpleResource>

    fun postSupportReply(
        replyId: Int,
    ): Flow<SimpleResource>
}
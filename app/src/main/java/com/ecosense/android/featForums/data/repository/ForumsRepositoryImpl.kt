package com.ecosense.android.featForums.data.repository

import com.ecosense.android.R
import com.ecosense.android.core.domain.api.AuthApi
import com.ecosense.android.core.domain.model.Story
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.SimpleResource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featForums.data.api.ForumsApi
import com.ecosense.android.featForums.data.model.GetRepliesDto
import com.ecosense.android.featForums.data.model.GetStorySupportersDto
import com.ecosense.android.featForums.domain.model.Reply
import com.ecosense.android.featForums.domain.model.Supporter
import com.ecosense.android.featForums.domain.repository.ForumsRepository
import com.ecosense.android.featProfile.data.model.ProfileDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import logcat.asLog
import logcat.logcat
import retrofit2.HttpException
import java.io.File
import java.io.IOException

class ForumsRepositoryImpl(
    private val authApi: AuthApi,
    private val forumsApi: ForumsApi,
) : ForumsRepository {

    override suspend fun getStories(
        page: Int,
        size: Int,
    ): Resource<List<Story>> {
        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            val response = forumsApi.getStories(
                bearerToken = bearerToken,
                page = page,
                size = size,
            )

            when {
                response.error == true -> return Resource.Error(uiText = response.message?.let {
                    UIText.DynamicString(it)
                } ?: UIText.StringResource(R.string.em_unknown))

                response.stories == null -> {
                    return Resource.Error(UIText.StringResource(R.string.em_unknown))
                }

                else -> return Resource.Success(response.stories.map { it.toDomain() })
            }

        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<ProfileDto>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<ProfileDto>() {}.type
                        )
                        UIText.DynamicString(response.message!!)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.em_unknown)
                    }
                }

                is IOException -> UIText.StringResource(R.string.em_io_exception)

                else -> UIText.StringResource(R.string.em_unknown)

            }.let { return Resource.Error(it) }
        }
    }

    override suspend fun getStoryReplies(
        storyId: Int,
        page: Int,
        size: Int,
    ): Resource<List<Reply>> {
        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            val response = forumsApi.getStoryReplies(
                bearerToken = bearerToken,
                storyId = storyId,
                page = page,
                size = size,
            )

            when {
                response.error == true -> return Resource.Error(uiText = response.message?.let {
                    UIText.DynamicString(it)
                } ?: UIText.StringResource(R.string.em_unknown))

                response.replies == null -> {
                    return Resource.Error(UIText.StringResource(R.string.em_unknown))
                }

                else -> return Resource.Success(response.replies.map { it.toDomain() })
            }

        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<GetRepliesDto>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<GetRepliesDto>() {}.type
                        )
                        UIText.DynamicString(response.message!!)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.em_unknown)
                    }
                }

                is IOException -> UIText.StringResource(R.string.em_io_exception)

                else -> UIText.StringResource(R.string.em_unknown)

            }.let { return Resource.Error(it) }
        }
    }

    override suspend fun getStorySupporters(
        storyId: Int,
        page: Int,
        size: Int,
    ): Resource<List<Supporter>> {
        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            val response = forumsApi.getStorySupporters(
                bearerToken = bearerToken,
                storyId = storyId,
                page = page,
                size = size,
            )

            when {
                response.error == true -> return Resource.Error(uiText = response.message?.let {
                    UIText.DynamicString(it)
                } ?: UIText.StringResource(R.string.em_unknown))

                response.supporters == null -> {
                    return Resource.Error(UIText.StringResource(R.string.em_unknown))
                }

                else -> return Resource.Success(response.supporters.map { it.toDomain() })
            }

        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<GetStorySupportersDto>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<GetStorySupportersDto>() {}.type
                        )
                        UIText.DynamicString(response.message!!)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.em_unknown)
                    }
                }

                is IOException -> UIText.StringResource(R.string.em_io_exception)

                else -> UIText.StringResource(R.string.em_unknown)

            }.let { return Resource.Error(it) }
        }
    }

    override fun postNewStory(
        caption: String, attachedPhoto: File?, sharedCampaignId: Int?
    ): Flow<SimpleResource> = flow {
        // TODO: not yet implemented
    }

    override fun postNewReply(
        storyId: Int, caption: String, attachedPhoto: File?
    ): Flow<SimpleResource> = flow {
        // TODO: not yet implemented
    }

    override fun postSupportStory(
        storyId: Int,
    ): Flow<SimpleResource> = flow {
        // TODO: not yet implemented
    }

    override fun postSupportReply(
        replyId: Int,
    ): Flow<SimpleResource> = flow {
        // TODO: not yet implemented
    }
}
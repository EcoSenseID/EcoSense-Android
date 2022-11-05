package com.ecosense.android.featProfile.data.repository

import android.net.Uri
import com.ecosense.android.R
import com.ecosense.android.core.domain.api.AuthApi
import com.ecosense.android.core.domain.api.CloudStorageApi
import com.ecosense.android.core.domain.model.Story
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.SimpleResource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featProfile.data.api.ProfileApi
import com.ecosense.android.featProfile.domain.model.Profile
import com.ecosense.android.featProfile.domain.model.RecentCampaign
import com.ecosense.android.featProfile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import logcat.asLog
import logcat.logcat
import java.io.IOException

class ProfileRepositoryImpl(
    private val authApi: AuthApi,
    private val profileApi: ProfileApi,
    private val cloudStorageApi: CloudStorageApi,
) : ProfileRepository {

    override fun getProfile(): Flow<Resource<Profile>> = flow {
        emit(Resource.Loading())

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            val response = profileApi.getProfile(bearerToken = bearerToken)

            when (response.error) {
                true -> emit(Resource.Error(uiText = response.message?.let {
                    UIText.DynamicString(it)
                } ?: UIText.StringResource(R.string.em_unknown)))
                else -> emit(Resource.Success(response.toDomain()))
            }

        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is IOException -> UIText.StringResource(R.string.em_io_exception)
                else -> UIText.StringResource(R.string.em_unknown)
            }.let { emit(Resource.Error(it)) }
        }
    }

    override fun updateProfile(
        newDisplayName: String?, newPhotoUri: Uri?
    ): Flow<SimpleResource> = flow {
        emit(Resource.Loading())

        when {
            newDisplayName == null && newPhotoUri == null -> {
                emit(Resource.Success(Unit))
                return@flow
            }

            newDisplayName != null && newDisplayName.isBlank() -> {
                emit(Resource.Error(UIText.StringResource(R.string.em_name_blank)))
                return@flow
            }
        }

        try {
            val uploadedPhotoUri: Uri? = newPhotoUri?.let { uri ->
                cloudStorageApi.uploadProfilePicture(
                    photoUri = uri, uid = authApi.getCurrentUser()?.uid ?: return@let null
                )
            }

            authApi.updateProfile(
                newDisplayName = newDisplayName,
                newPhotoUri = uploadedPhotoUri,
            ).also { emit(it) }
        } catch (e: Exception) {
            logcat { e.asLog() }
            emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
        }
    }

    override suspend fun sendEmailVerification(): Flow<SimpleResource> = flow {
        emit(Resource.Loading())
        try {
            authApi.sendEmailVerification().also { emit(it) }
        } catch (e: Exception) {
            logcat { e.asLog() }
            emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
        }
    }

    override fun getStoriesHistory(): Flow<Resource<List<Story>>> = flow {
        emit(Resource.Loading())

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            val response = profileApi.getStoriesHistory(bearerToken = bearerToken)

            when {
                response.error == true -> emit(Resource.Error(uiText = response.message?.let {
                    UIText.DynamicString(it)
                } ?: UIText.StringResource(R.string.em_unknown)))

                response.stories == null -> {
                    emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
                }

                else -> emit(Resource.Success(response.stories.map { it.toDomain() }))
            }

        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is IOException -> UIText.StringResource(R.string.em_io_exception)
                else -> UIText.StringResource(R.string.em_unknown)
            }.let { emit(Resource.Error(it)) }
        }
    }

    override fun getCampaignsHistory(): Flow<Resource<List<RecentCampaign>>> = flow {
        emit(Resource.Loading())

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            val response = profileApi.getCampaignsHistory(bearerToken = bearerToken)

            when {
                response.error == true -> emit(Resource.Error(uiText = response.message?.let {
                    UIText.DynamicString(it)
                } ?: UIText.StringResource(R.string.em_unknown)))

                response.campaigns == null -> {
                    emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
                }

                else -> emit(Resource.Success(response.campaigns.map { it.toDomain() }))
            }

        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is IOException -> UIText.StringResource(R.string.em_io_exception)
                else -> UIText.StringResource(R.string.em_unknown)
            }.let { emit(Resource.Error(it)) }
        }
    }
}
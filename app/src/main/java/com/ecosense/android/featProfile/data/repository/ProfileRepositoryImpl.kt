package com.ecosense.android.featProfile.data.repository

import android.net.Uri
import com.ecosense.android.R
import com.ecosense.android.core.domain.api.AuthApi
import com.ecosense.android.core.domain.api.CloudStorageApi
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.SimpleResource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featProfile.data.api.ProfileApi
import com.ecosense.android.featProfile.data.model.ContributionsDto
import com.ecosense.android.featProfile.domain.model.Contributions
import com.ecosense.android.featProfile.domain.repository.ProfileRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import logcat.asLog
import logcat.logcat
import retrofit2.HttpException
import java.io.IOException

class ProfileRepositoryImpl(
    private val authApi: AuthApi,
    private val profileApi: ProfileApi,
    private val cloudStorageApi: CloudStorageApi,
) : ProfileRepository {
    override fun getContributions(): Flow<Resource<Contributions>> = flow {
        emit(Resource.Loading())

        try {
            val idToken = authApi.getIdToken(true)
//            val bearerToken = "Bearer $idToken"
            val bearerToken = "Bearer <idToken>"
            val response = profileApi.getContributions(bearerToken = bearerToken)

            when {
                response.error == true -> emit(Resource.Error(
                    uiText = response.message?.let { UIText.DynamicString(it) }
                        ?: UIText.StringResource(R.string.em_unknown))
                )

                response.experiences.isNullOrEmpty() ||
                        response.completedCampaigns.isNullOrEmpty() -> {
                    emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
                }

                else -> emit(
                    Resource.Success(
                        Contributions(
                            experiences = response.experiences.map { it.toExperience() },
                            completedCampaigns = response.completedCampaigns.map { it.toCampaign() }
                        )
                    )
                )
            }

        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<ContributionsDto>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<ContributionsDto>() {}.type
                        )
                        UIText.DynamicString(response.message!!)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.em_unknown)
                    }
                }

                is IOException -> UIText.StringResource(R.string.em_io_exception)

                else -> UIText.StringResource(R.string.em_unknown)

            }.let { emit(Resource.Error(it)) }
        }
    }

    override fun updateProfile(
        newDisplayName: String?,
        newPhotoUri: Uri?
    ): Flow<SimpleResource> = flow {
        emit(Resource.Loading())

        if (newDisplayName == null && newPhotoUri == null) {
            emit(Resource.Success(Unit))
            return@flow
        }

        val uploadedPhotoUri: Uri? = newPhotoUri?.let { uri ->
            cloudStorageApi.uploadProfilePicture(
                photoUri = uri,
                uid = authApi.getCurrentUser().first()?.uid ?: return@let null
            )
        }

        try {
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
            emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
        }
    }
}
package com.ecosense.android.featProfile.domain.repository

import android.net.Uri
import com.ecosense.android.core.domain.model.Story
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.SimpleResource
import com.ecosense.android.featProfile.domain.model.OthersProfile
import com.ecosense.android.featProfile.domain.model.Profile
import com.ecosense.android.featProfile.domain.model.RecentCampaign
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<Resource<Profile>>

    fun getOthersProfile(
        userId: Int,
    ): Flow<Resource<OthersProfile>>

    fun updateProfile(
        newDisplayName: String?, newPhotoUri: Uri?
    ): Flow<SimpleResource>

    suspend fun sendEmailVerification(): Flow<SimpleResource>

    fun getStoriesHistory(
        userId: Int?,
    ): Flow<Resource<List<Story>>>

    fun getCampaignsHistory(
        userId: Int?,
    ): Flow<Resource<List<RecentCampaign>>>
}
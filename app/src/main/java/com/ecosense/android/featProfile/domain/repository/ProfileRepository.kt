package com.ecosense.android.featProfile.domain.repository

import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.SimpleResource
import com.ecosense.android.featProfile.domain.model.Contributions
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getContributions(): Flow<Resource<Contributions>>

    fun updateProfile(
        displayName: String?,
        photoUrl: String?,
    ): Flow<SimpleResource>

    suspend fun sendEmailVerification(): Flow<SimpleResource>
}
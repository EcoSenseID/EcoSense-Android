package com.ecosense.android.features.profile.domain.repository

import com.ecosense.android.core.util.Resource
import com.ecosense.android.features.profile.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<Resource<Profile>>
}
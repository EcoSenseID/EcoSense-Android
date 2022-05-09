package com.ecosense.android.featProfile.domain.repository

import com.ecosense.android.core.util.Resource
import com.ecosense.android.featProfile.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<Resource<Profile>>
}
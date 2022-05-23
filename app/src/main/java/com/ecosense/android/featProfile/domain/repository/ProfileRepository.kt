package com.ecosense.android.featProfile.domain.repository

import com.ecosense.android.core.util.Resource
import com.ecosense.android.featProfile.domain.model.Contributions
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getContributions(): Flow<Resource<Contributions>>
}
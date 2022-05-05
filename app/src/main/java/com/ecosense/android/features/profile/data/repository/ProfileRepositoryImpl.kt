package com.ecosense.android.features.profile.data.repository

import com.ecosense.android.core.util.Resource
import com.ecosense.android.features.profile.domain.model.Profile
import com.ecosense.android.features.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProfileRepositoryImpl : ProfileRepository {
    override fun getProfile(): Flow<Resource<Profile>> = flow {
        // TODO: not yet implemented
    }
}
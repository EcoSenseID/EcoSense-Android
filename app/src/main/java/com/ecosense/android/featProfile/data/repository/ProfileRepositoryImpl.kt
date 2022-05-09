package com.ecosense.android.featProfile.data.repository

import com.ecosense.android.core.util.Resource
import com.ecosense.android.featProfile.domain.model.Profile
import com.ecosense.android.featProfile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProfileRepositoryImpl : ProfileRepository {
    override fun getProfile(): Flow<Resource<Profile>> = flow {
        // TODO: not yet implemented
    }
}
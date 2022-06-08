package com.ecosense.android.featProfile.data.repository

import android.net.Uri
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.SimpleResource
import com.ecosense.android.featProfile.domain.model.Contributions
import com.ecosense.android.featProfile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeProfileRepository(
    private val fakeContributions: Resource<Contributions>? = null,
    private val fakeUpdateProfileResult: SimpleResource? = null,
    private val fakeSendEmailVerificationResult: SimpleResource? = null,
) : ProfileRepository {
    override fun getContributions(): Flow<Resource<Contributions>> = flow {
        emit(fakeContributions ?: throw NullPointerException())
    }

    override fun updateProfile(
        newDisplayName: String?,
        newPhotoUri: Uri?
    ): Flow<SimpleResource> = flow {
        emit(fakeUpdateProfileResult ?: throw NullPointerException())
    }

    override suspend fun sendEmailVerification(): Flow<SimpleResource> = flow {
        emit(fakeSendEmailVerificationResult ?: throw NullPointerException())
    }
}
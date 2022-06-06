package com.ecosense.android.featProfile.data.api

import com.ecosense.android.featProfile.data.model.ContributionsDto

class FakeProfileApi(
    private val contributionsDto: ContributionsDto? = null,
    private val exception: Exception? = null,
) : ProfileApi {
    override suspend fun getContributions(bearerToken: String): ContributionsDto {
        return contributionsDto ?: throw exception ?: throw NullPointerException()
    }
}
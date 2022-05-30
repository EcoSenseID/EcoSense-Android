package com.ecosense.android.featProfile.data.api

import com.ecosense.android.featProfile.data.model.ContributionsDto
import retrofit2.http.GET
import retrofit2.http.Header

interface ProfileApi {
    @GET("contributions")
    suspend fun getContributions(
        @Header("Authorization") bearerToken: String,
    ): ContributionsDto
}
package com.ecosense.android.featProfile.data.api

import com.ecosense.android.featProfile.data.model.ProfileDto
import retrofit2.http.GET
import retrofit2.http.Header

interface ProfileApi {

    @GET("profile")
    suspend fun getProfile(
        @Header("Authorization") bearerToken: String,
    ): ProfileDto
}
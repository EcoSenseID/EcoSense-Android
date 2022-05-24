package com.ecosense.android.core.domain.api

import com.ecosense.android.core.domain.model.User
import com.ecosense.android.core.util.SimpleResource
import kotlinx.coroutines.flow.Flow

interface AuthApi {
    fun getCurrentUser(): Flow<User?>

    suspend fun getIdToken(
        forceRefresh: Boolean,
    ): String?

    suspend fun loginWithEmail(
        email: String,
        password: String,
    ): SimpleResource

    suspend fun loginWithGoogle(
        idToken: String?,
    ): SimpleResource

    suspend fun registerWithEmail(
        email: String,
        password: String,
    ): SimpleResource

    suspend fun sendPasswordResetEmail(
        email: String,
    ): SimpleResource

    fun logout()

    suspend fun updateProfile(
        displayName: String?,
        photoUrl: String?,
    ): SimpleResource

    suspend fun sendEmailVerification(): SimpleResource
}
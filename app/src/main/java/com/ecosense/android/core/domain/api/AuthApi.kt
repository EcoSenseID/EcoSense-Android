package com.ecosense.android.core.domain.api

import android.net.Uri
import com.ecosense.android.core.domain.model.User
import com.ecosense.android.core.util.SimpleResource
import kotlinx.coroutines.flow.Flow

interface AuthApi {
    val isLoggedIn: Flow<Boolean>

    suspend fun getCurrentUser(): User?

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
        newDisplayName: String?,
        newPhotoUri: Uri?,
    ): SimpleResource

    suspend fun updatePassword(
        oldPassword: String,
        newPassword: String,
    ): SimpleResource

    suspend fun updateEmail(
        password: String,
        newEmail: String,
    ): SimpleResource

    suspend fun sendEmailVerification(): SimpleResource
}
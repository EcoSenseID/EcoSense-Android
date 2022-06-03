package com.ecosense.android.core.domain.api

import android.net.Uri
import com.ecosense.android.core.domain.model.User
import com.ecosense.android.core.util.SimpleResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthApi {
    val isLoggedIn: Flow<Boolean>

    val currentUser: StateFlow<User?>

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

    suspend fun sendEmailVerification(): SimpleResource
}
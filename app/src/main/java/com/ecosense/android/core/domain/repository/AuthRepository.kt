package com.ecosense.android.core.domain.repository

import com.ecosense.android.core.domain.model.User
import com.ecosense.android.core.util.SimpleResource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isLoggedIn: Flow<Boolean>

    suspend fun getCurrentUser(): User?

    fun loginWithEmail(
        email: String,
        password: String,
    ): Flow<SimpleResource>

    fun loginWithGoogle(
        idToken: String?,
    ): Flow<SimpleResource>

    fun registerWithEmail(
        name: String,
        email: String,
        password: String,
        repeatedPassword: String,
    ): Flow<SimpleResource>

    fun sendPasswordResetEmail(
        email: String,
    ): Flow<SimpleResource>

    fun updatePassword(
        oldPassword: String,
        newPassword: String,
        repeatedNewPassword: String,
    ): Flow<SimpleResource>

    fun updateEmail(
        password: String,
        newEmail: String,
    ): Flow<SimpleResource>

    fun logout()
}
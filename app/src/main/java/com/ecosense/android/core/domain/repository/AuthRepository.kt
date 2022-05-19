package com.ecosense.android.core.domain.repository

import com.ecosense.android.core.domain.model.User
import com.ecosense.android.core.util.SimpleResource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getUser(): Flow<User>

    fun isLoggedIn(): Flow<Boolean>

    fun loginWithEmail(
        email: String,
        password: String
    ): Flow<SimpleResource>

    fun loginWithGoogle(
        idToken: String
    ): Flow<SimpleResource>

    fun registerWithEmail(
        email: String,
        password: String,
        passwordVerif: String
    ): Flow<SimpleResource>

    fun logout()
}
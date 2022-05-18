package com.ecosense.android.core.domain.repository

import com.ecosense.android.core.domain.model.User
import com.ecosense.android.core.util.SimpleResource
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    fun getUser(): Flow<User>
    fun isLoggedIn(): Flow<Boolean>
    fun login(email: String, password: String): Flow<SimpleResource>
    fun loginWithGoogle(): Flow<SimpleResource>
    fun register(email: String, password: String): Flow<SimpleResource>
    fun logout()
}
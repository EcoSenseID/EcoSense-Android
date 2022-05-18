package com.ecosense.android.core.domain.repository

import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.SimpleResource
import com.ecosense.android.core.domain.model.LoginResult
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    // TODO : add Google OAuth
    fun isLoggedIn(): Flow<Boolean>
    fun login(email: String, password: String): Flow<Resource<LoginResult>>
    fun register(name: String, email: String, password: String): Flow<SimpleResource>
}
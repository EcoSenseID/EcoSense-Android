package com.ecosense.android.featAuthentication.data.repository

import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.SimpleResource
import com.ecosense.android.featAuthentication.domain.model.LoginResult
import com.ecosense.android.featAuthentication.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthenticationRepositoryImpl : AuthenticationRepository {
    override fun login(
        email: String,
        password: String
    ): Flow<Resource<LoginResult>> = flow {
        // TODO: implement login with email
    }

    override fun register(
        name: String,
        email: String,
        password: String
    ): Flow<SimpleResource> = flow {
        // TODO: implement register with email
    }
}
package com.ecosense.android.core.data.repository

import com.ecosense.android.core.domain.model.LoginResult
import com.ecosense.android.core.domain.repository.AuthenticationRepository
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.SimpleResource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

class AuthenticationRepositoryImpl : AuthenticationRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun isLoggedIn(): Flow<Boolean> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener {
            trySend(it.currentUser != null)
        }

        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose { firebaseAuth.removeAuthStateListener(authStateListener) }
    }

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
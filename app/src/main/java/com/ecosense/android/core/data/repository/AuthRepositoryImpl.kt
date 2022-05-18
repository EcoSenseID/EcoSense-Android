package com.ecosense.android.core.data.repository

import android.util.Log
import com.ecosense.android.R
import com.ecosense.android.core.data.util.toUser
import com.ecosense.android.core.domain.model.User
import com.ecosense.android.core.domain.repository.AuthRepository
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.SimpleResource
import com.ecosense.android.core.util.UIText
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthRepositoryImpl : AuthRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun getUser(): Flow<User> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener {
            it.currentUser?.let { firebaseUser ->
                trySend(firebaseUser.toUser())
            }
        }

        firebaseAuth.addAuthStateListener(authStateListener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
            channel.close()
        }
    }

    override fun isLoggedIn(): Flow<Boolean> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener {
            trySend(it.currentUser != null)
        }

        firebaseAuth.addAuthStateListener(authStateListener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
            channel.close()
        }
    }

    override fun login(
        email: String,
        password: String
    ): Flow<SimpleResource> = callbackFlow {
        firebaseAuth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                when {
                    it.isSuccessful -> trySend(Resource.Success(Unit))
                    else -> {
                        Log.d("AuthRepo", "login: ${it.exception}")
                        trySend(Resource.Error(UIText.StringResource(R.string.login_failed)))
                    }
                }
            }
        awaitClose {
            channel.close()
        }
    }

    override fun loginWithGoogle(): Flow<SimpleResource> = callbackFlow {
        // TODO implement
    }

    override fun register(
        email: String,
        password: String
    ): Flow<SimpleResource> = callbackFlow {
        firebaseAuth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                when {
                    it.isSuccessful -> trySend(Resource.Success(Unit))
                    else -> {
                        Log.d("AuthRepo", "login: ${it.exception}")
                        trySend(Resource.Error(UIText.StringResource(R.string.register_failed)))
                    }
                }
            }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}
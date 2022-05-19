package com.ecosense.android.core.data.repository

import android.util.Log
import com.ecosense.android.R
import com.ecosense.android.core.data.util.toUser
import com.ecosense.android.core.domain.model.User
import com.ecosense.android.core.domain.repository.AuthRepository
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.SimpleResource
import com.ecosense.android.core.util.UIText
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

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
    ): Flow<SimpleResource> = when {

        email.isBlank() -> flow {
            emit(Resource.Error(UIText.StringResource(R.string.em_email_blank)))
        }

        password.isBlank() -> flow {
            emit(Resource.Error(UIText.StringResource(R.string.em_password_blank)))
        }

        else -> callbackFlow {
            trySend(Resource.Loading())

            try {
                val onCompleteListener = OnCompleteListener<AuthResult> {
                    when {
                        it.isSuccessful -> trySend(Resource.Success(Unit))
                        else -> {
                            trySend(Resource.Error(UIText.StringResource(R.string.login_failed)))
                            Log.d("AuthRepo", "login: ${it.exception}")
                        }
                    }
                }

                val onFailureListener = OnFailureListener {
                    trySend(Resource.Error(UIText.StringResource(R.string.login_failed)))
                    Log.d("AuthRepo", "login: ${it.message}")
                }

                firebaseAuth
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(onCompleteListener)
                    .addOnFailureListener(onFailureListener)
            } catch (t: Throwable) {
                trySend(Resource.Error(UIText.StringResource(R.string.login_failed)))
                Log.d("TAG", "login: ${t.message}")
            }

            awaitClose { channel.close() }
        }
    }

    override fun loginWithGoogle(): Flow<SimpleResource> = callbackFlow {
        // TODO implement
    }

    override fun register(
        email: String,
        password: String,
        passwordVerif: String
    ): Flow<SimpleResource> = when {

        email.isBlank() -> flow {
            emit(Resource.Error(UIText.StringResource(R.string.em_email_blank)))
        }

        password.isBlank() -> flow {
            emit(Resource.Error(UIText.StringResource(R.string.em_password_blank)))
        }

        !password.contentEquals(passwordVerif) -> flow {
            emit(Resource.Error(UIText.StringResource(R.string.em_password_not_match)))
        }

        else -> callbackFlow {
            trySend(Resource.Loading())

            try {
                val onCompleteListener = OnCompleteListener<AuthResult> {
                    when {
                        it.isSuccessful -> trySend(Resource.Success(Unit))
                        else -> {
                            trySend(Resource.Error(UIText.StringResource(R.string.login_failed)))
                            Log.d("AuthRepo", "register: ${it.exception}")
                        }
                    }
                }

                val onFailureListener = OnFailureListener {
                    trySend(Resource.Error(UIText.StringResource(R.string.login_failed)))
                    Log.d("AuthRepo", "register: ${it.message}")
                }

                firebaseAuth
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(onCompleteListener)
                    .addOnFailureListener(onFailureListener)
            } catch (t: Throwable) {
                trySend(Resource.Error(UIText.StringResource(R.string.login_failed)))
                Log.d("TAG", "register: ${t.message}")
            }

            awaitClose { channel.close() }
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}
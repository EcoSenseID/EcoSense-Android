package com.ecosense.android.core.data.repository

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
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import logcat.asLog
import logcat.logcat

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

    override fun loginWithEmail(
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
                            logcat { "loginWithEmail: ${it.exception?.asLog()}" }
                        }
                    }
                }

                val onFailureListener = OnFailureListener {
                    trySend(Resource.Error(UIText.StringResource(R.string.login_failed)))
                    logcat { "loginWithEmail: ${it.asLog()}" }
                }

                firebaseAuth
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(onCompleteListener)
                    .addOnFailureListener(onFailureListener)
            } catch (t: Throwable) {
                trySend(Resource.Error(UIText.StringResource(R.string.login_failed)))
                logcat { "loginWithEmail: ${t.asLog()}" }

            }

            awaitClose { channel.close() }
        }
    }

    override fun loginWithGoogle(
        idToken: String
    ): Flow<SimpleResource> = callbackFlow {

        trySend(Resource.Loading())

        try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)

            val onCompleteListener = OnCompleteListener<AuthResult> {
                when {
                    it.isSuccessful -> trySend(Resource.Success(Unit))
                    else -> {
                        trySend(Resource.Error(UIText.StringResource(R.string.login_failed)))
                        logcat { "loginWithGoogle: ${it.exception?.asLog()}" }
                    }
                }
            }

            val onFailureListener = OnFailureListener {
                trySend(Resource.Error(UIText.StringResource(R.string.login_failed)))
                logcat { "loginWithGoogle: ${it.asLog()}" }

            }

            firebaseAuth
                .signInWithCredential(credential)
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener)

        } catch (t: Throwable) {
            trySend(Resource.Error(UIText.StringResource(R.string.login_failed)))
            logcat { "loginWithGoogle: ${t.asLog()}" }
        }

        awaitClose { channel.close() }
    }

    override fun registerWithEmail(
        email: String,
        password: String,
        repeatedPassword: String
    ): Flow<SimpleResource> = when {

        email.isBlank() -> flow {
            emit(Resource.Error(UIText.StringResource(R.string.em_email_blank)))
        }

        password.isBlank() -> flow {
            emit(Resource.Error(UIText.StringResource(R.string.em_password_blank)))
        }

        !password.contentEquals(repeatedPassword) -> flow {
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
                            logcat { "registerWithEmail: ${it.exception?.asLog()}" }
                        }
                    }
                }

                val onFailureListener = OnFailureListener {
                    trySend(Resource.Error(UIText.StringResource(R.string.login_failed)))
                    logcat { "registerWithEmail: ${it.asLog()}" }
                }

                firebaseAuth
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(onCompleteListener)
                    .addOnFailureListener(onFailureListener)
            } catch (t: Throwable) {
                trySend(Resource.Error(UIText.StringResource(R.string.login_failed)))
                logcat { "registerWithEmail: ${t.asLog()}" }

            }

            awaitClose { channel.close() }
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun sendPasswordResetEmail(
        email: String
    ): Flow<SimpleResource> = when {
        email.isBlank() -> flow {
            emit(Resource.Error(UIText.StringResource(R.string.em_email_blank)))
        }

        else -> callbackFlow {
            trySend(Resource.Loading())

            try {
                val onCompleteListener = OnCompleteListener<Void> {
                    trySend(
                        if (it.isSuccessful) Resource.Success(Unit)
                        else Resource.Error(UIText.StringResource(R.string.em_unknown))
                    )

                    logcat { "sendPasswordResetEmail: ${it.exception?.asLog()}" }
                }

                val onFailureListener = OnFailureListener {
                    trySend(Resource.Error(UIText.StringResource(R.string.em_unknown)))
                    logcat { "sendPasswordResetEmail: ${it.asLog()}" }
                }

                firebaseAuth
                    .sendPasswordResetEmail(email)
                    .addOnCompleteListener(onCompleteListener)
                    .addOnFailureListener(onFailureListener)

            } catch (t: Throwable) {
                trySend(Resource.Error(UIText.StringResource(R.string.em_unknown)))
                logcat { "sendPasswordResetEmail: ${t.asLog()}" }
            }

            awaitClose { channel.close() }
        }
    }
}
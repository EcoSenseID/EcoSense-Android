package com.ecosense.android.core.data.repository

import android.util.Patterns
import com.ecosense.android.R
import com.ecosense.android.core.data.util.toUser
import com.ecosense.android.core.domain.model.User
import com.ecosense.android.core.domain.repository.AuthRepository
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.SimpleResource
import com.ecosense.android.core.util.UIText
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.*
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
            it.currentUser?.let { firebaseUser -> trySend(firebaseUser.toUser()) }
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

        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> flow {
            emit(Resource.Error(UIText.StringResource(R.string.em_invalid_email)))
        }

        password.isBlank() -> flow {
            emit(Resource.Error(UIText.StringResource(R.string.em_password_blank)))
        }

        else -> callbackFlow {
            trySend(Resource.Loading())

            try {
                val onCompleteListener = OnCompleteListener<AuthResult> {
                    trySend(
                        if (it.isSuccessful) Resource.Success(Unit)
                        else Resource.Error(UIText.StringResource(R.string.em_unknown))
                    )
                }

                firebaseAuth
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(onCompleteListener)

            } catch (t: Throwable) {
                UIText.StringResource(
                    when (t) {
                        is FirebaseAuthInvalidUserException -> R.string.em_email_login_invalid_user
                        is FirebaseAuthInvalidCredentialsException -> R.string.wrong_password
                        else -> R.string.em_unknown
                    }
                )
                    .let { Resource.Error<Unit>(it) }
                    .let { trySend(it) }

                logcat { t.asLog() }
            }

            awaitClose { channel.close() }
        }
    }

    override fun loginWithGoogle(
        idToken: String?
    ): Flow<SimpleResource> = when {

        idToken.isNullOrBlank() -> flow {
            emit(Resource.Error(UIText.StringResource(R.string.em_google_sign_in)))
        }

        else -> callbackFlow {
            trySend(Resource.Loading())

            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)

                val onCompleteListener = OnCompleteListener<AuthResult> {
                    trySend(
                        if (it.isSuccessful) Resource.Success(Unit)
                        else Resource.Error(UIText.StringResource(R.string.em_unknown))
                    )
                }

                firebaseAuth
                    .signInWithCredential(credential)
                    .addOnCompleteListener(onCompleteListener)

            } catch (t: Throwable) {
                UIText.StringResource(
                    when (t) {
                        is FirebaseAuthInvalidUserException -> R.string.em_google_sign_in_invalid_user
                        is FirebaseAuthInvalidCredentialsException -> R.string.em_google_sign_in_invalid_credential
                        is FirebaseAuthUserCollisionException -> R.string.em_google_sign_in_collision
                        else -> R.string.em_unknown
                    }
                )
                    .let { Resource.Error<Unit>(it) }
                    .let { trySend(it) }

                logcat { t.asLog() }
            }

            awaitClose { channel.close() }
        }
    }

    override fun registerWithEmail(
        email: String,
        password: String,
        repeatedPassword: String
    ): Flow<SimpleResource> = when {

        email.isBlank() -> flow {
            emit(Resource.Error(UIText.StringResource(R.string.em_email_blank)))
        }

        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> flow {
            emit(Resource.Error(UIText.StringResource(R.string.em_invalid_email)))
        }

        password.isBlank() -> flow {
            emit(Resource.Error(UIText.StringResource(R.string.em_password_blank)))
        }

        password.length < 6 -> flow {
            emit(Resource.Error(UIText.StringResource(R.string.em_register_password_too_short)))
        }

        !password.contentEquals(repeatedPassword) -> flow {
            emit(Resource.Error(UIText.StringResource(R.string.em_password_not_match)))
        }

        else -> callbackFlow {
            trySend(Resource.Loading())

            try {
                val onCompleteListener = OnCompleteListener<AuthResult> {
                    trySend(
                        if (it.isSuccessful) Resource.Success(Unit)
                        else Resource.Error(UIText.StringResource(R.string.em_unknown))
                    )
                }

                firebaseAuth
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(onCompleteListener)

            } catch (t: Throwable) {
                UIText.StringResource(
                    when (t) {
                        is FirebaseAuthWeakPasswordException -> R.string.em_register_weak_password
                        is FirebaseAuthInvalidCredentialsException -> R.string.em_register_invalid_email
                        is FirebaseAuthUserCollisionException -> R.string.em_register_email_registered
                        else -> R.string.em_unknown
                    }
                )
                    .let { Resource.Error<Unit>(it) }
                    .let { trySend(it) }

                logcat { t.asLog() }
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

        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> flow {
            emit(Resource.Error(UIText.StringResource(R.string.em_invalid_email)))
        }

        else -> callbackFlow {
            trySend(Resource.Loading())

            try {
                val onCompleteListener = OnCompleteListener<Void> {
                    trySend(
                        if (it.isSuccessful) Resource.Success(Unit)
                        else Resource.Error(UIText.StringResource(R.string.em_unknown))
                    )
                }

                firebaseAuth
                    .sendPasswordResetEmail(email)
                    .addOnCompleteListener(onCompleteListener)
            } catch (t: Throwable) {
                UIText.StringResource(
                    when (t) {
                        is FirebaseAuthInvalidUserException -> R.string.em_reset_password_unregistered_email
                        else -> R.string.em_unknown
                    }
                )
                    .let { Resource.Error<Unit>(it) }
                    .let { trySend(it) }

                logcat { t.asLog() }
            }

            awaitClose { channel.close() }
        }
    }
}
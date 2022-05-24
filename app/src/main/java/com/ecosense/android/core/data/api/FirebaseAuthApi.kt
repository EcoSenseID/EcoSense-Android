package com.ecosense.android.core.data.api

import android.net.Uri
import com.ecosense.android.R
import com.ecosense.android.core.domain.api.AuthApi
import com.ecosense.android.core.domain.model.User
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.SimpleResource
import com.ecosense.android.core.util.UIText
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthApi : AuthApi {

    private val firebaseAuth = FirebaseAuth.getInstance().apply { useAppLanguage() }

    override val isLoggedIn: Flow<Boolean>
        get() = callbackFlow {
            val authStateListener = FirebaseAuth.AuthStateListener {
                trySend(it.currentUser != null)
            }

            firebaseAuth.addAuthStateListener(authStateListener)

            awaitClose {
                firebaseAuth.removeAuthStateListener(authStateListener)
                channel.close()
            }
        }

    override suspend fun getCurrentUser(): User? = suspendCoroutine { cont ->
        firebaseAuth.currentUser?.reload()?.addOnCompleteListener { task ->
            val firebaseUser = firebaseAuth.currentUser
            when {
                task.isSuccessful -> cont.resume(
                    User(
                        uid = firebaseUser?.uid,
                        displayName = firebaseUser?.displayName,
                        email = firebaseUser?.email,
                        photoUrl = firebaseUser?.photoUrl?.toString(),
                        isEmailVerified = firebaseUser?.isEmailVerified
                    )
                )
                else -> cont.resume(null)
            }
        }
    }


    override suspend fun getIdToken(
        forceRefresh: Boolean,
    ): String? = suspendCoroutine { cont ->
        firebaseAuth
            .currentUser
            ?.getIdToken(forceRefresh)
            ?.addOnCompleteListener { task ->
                when {
                    task.isSuccessful -> cont.resume(task.result.token)
                    else -> cont.resume(null)
                }
            }
    }

    override suspend fun loginWithEmail(
        email: String,
        password: String,
    ): SimpleResource = suspendCoroutine { cont ->
        firebaseAuth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                when {
                    task.isSuccessful -> cont.resume(Resource.Success(Unit))
                    else -> when (task.exception) {
                        is FirebaseAuthInvalidUserException -> R.string.em_email_login_invalid_user
                        is FirebaseAuthInvalidCredentialsException -> R.string.wrong_password
                        else -> R.string.em_unknown
                    }.let { cont.resume(Resource.Error(UIText.StringResource(it))) }
                }
            }
    }

    override suspend fun loginWithGoogle(
        idToken: String?
    ): SimpleResource = suspendCoroutine { cont ->
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        firebaseAuth
            .signInWithCredential(credential)
            .addOnCompleteListener { task ->
                when {
                    task.isSuccessful -> cont.resume(Resource.Success(Unit))
                    else -> when (task.exception) {
                        is FirebaseAuthInvalidUserException -> R.string.em_google_sign_in_invalid_user
                        is FirebaseAuthInvalidCredentialsException -> R.string.em_google_sign_in_invalid_credential
                        is FirebaseAuthUserCollisionException -> R.string.em_google_sign_in_collision
                        else -> R.string.em_unknown
                    }.let { cont.resume(Resource.Error(UIText.StringResource(it))) }
                }
            }
    }

    override suspend fun registerWithEmail(
        email: String,
        password: String,
    ): SimpleResource = suspendCoroutine { cont ->
        firebaseAuth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                when {
                    task.isSuccessful -> cont.resume(Resource.Success(Unit))
                    else -> when (task.exception) {
                        is FirebaseAuthWeakPasswordException -> R.string.em_register_weak_password
                        is FirebaseAuthInvalidCredentialsException -> R.string.em_register_invalid_email
                        is FirebaseAuthUserCollisionException -> R.string.em_register_email_registered
                        else -> R.string.em_unknown
                    }.let { cont.resume(Resource.Error(UIText.StringResource(it))) }
                }
            }
    }

    override suspend fun sendPasswordResetEmail(
        email: String,
    ): SimpleResource = suspendCoroutine { cont ->
        firebaseAuth
            .sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                when {
                    task.isSuccessful -> cont.resume(Resource.Success(Unit))
                    else -> when (task.exception) {
                        is FirebaseAuthInvalidUserException -> R.string.em_reset_password_unregistered_email
                        else -> R.string.em_unknown
                    }.let { cont.resume(Resource.Error(UIText.StringResource(it))) }
                }
            }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override suspend fun updateProfile(
        newDisplayName: String?,
        newPhotoUri: Uri?,
    ): SimpleResource = suspendCoroutine { cont ->
        val user = firebaseAuth.currentUser

        if (user == null) {
            cont.resume(Resource.Error(UIText.StringResource(R.string.em_unknown)))
            return@suspendCoroutine
        }

        val profileUpdates = userProfileChangeRequest {
            displayName = newDisplayName ?: user.displayName
            photoUri = newPhotoUri ?: user.photoUrl
        }

        user.updateProfile(profileUpdates).addOnCompleteListener { task ->
            when {
                task.isSuccessful -> cont.resume(Resource.Success(Unit))
                else -> when (task.exception) {
                    is FirebaseAuthInvalidUserException -> R.string.em_invalid_credentials
                    else -> R.string.em_unknown
                }.let { cont.resume(Resource.Error(UIText.StringResource(it))) }
            }
        }
    }

    override suspend fun sendEmailVerification(): SimpleResource = suspendCoroutine { cont ->
        val user = firebaseAuth.currentUser

        if (user == null) {
            cont.resume(Resource.Error(UIText.StringResource(R.string.em_unknown)))
            return@suspendCoroutine
        }

        user.sendEmailVerification().addOnCompleteListener { task ->
            when {
                task.isSuccessful -> cont.resume(Resource.Success(Unit))
                else -> when (task.exception) {
                    is FirebaseTooManyRequestsException -> R.string.em_too_many_request
                    else -> R.string.em_unknown
                }.let { cont.resume(Resource.Error(UIText.StringResource(it))) }
            }
        }
    }
}
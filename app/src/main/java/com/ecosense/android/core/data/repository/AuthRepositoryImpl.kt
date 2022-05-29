package com.ecosense.android.core.data.repository

import android.util.Patterns
import com.ecosense.android.R
import com.ecosense.android.core.domain.api.AuthApi
import com.ecosense.android.core.domain.model.User
import com.ecosense.android.core.domain.repository.AuthRepository
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.SimpleResource
import com.ecosense.android.core.util.UIText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl(
    private val authApi: AuthApi
) : AuthRepository {

    override val isLoggedIn: Flow<Boolean> = authApi.isLoggedIn

    override suspend fun getCurrentUser(): User? = authApi.getCurrentUser()

    override fun loginWithEmail(
        email: String,
        password: String,
    ): Flow<SimpleResource> = flow {
        emit(Resource.Loading())

        when {
            email.isBlank() -> {
                emit(Resource.Error(UIText.StringResource(R.string.em_email_blank)))
                return@flow
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                emit(Resource.Error(UIText.StringResource(R.string.em_invalid_email)))
                return@flow
            }
            password.isBlank() -> {
                emit(Resource.Error(UIText.StringResource(R.string.em_password_blank)))
                return@flow
            }
        }

        try {
            authApi
                .loginWithEmail(email = email, password = password)
                .also { emit(it) }
        } catch (e: Exception) {
            emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
        }
    }

    override fun loginWithGoogle(
        idToken: String?,
    ): Flow<SimpleResource> = flow {
        emit(Resource.Loading())

        when {
            idToken.isNullOrBlank() -> {
                emit(Resource.Error(UIText.StringResource(R.string.em_google_sign_in)))
                return@flow
            }
        }

        try {
            authApi
                .loginWithGoogle(idToken = idToken)
                .also { emit(it) }
        } catch (e: Exception) {
            emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
        }
    }

    override fun registerWithEmail(
        name: String,
        email: String,
        password: String,
        repeatedPassword: String,
    ): Flow<SimpleResource> = flow {
        emit(Resource.Loading())

        when {
            name.isBlank() -> {
                emit(Resource.Error(UIText.StringResource(R.string.em_name_blank)))
                return@flow
            }

            email.isBlank() -> {
                emit(Resource.Error(UIText.StringResource(R.string.em_email_blank)))
                return@flow
            }

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                emit(Resource.Error(UIText.StringResource(R.string.em_invalid_email)))
                return@flow
            }

            password.isBlank() -> {
                emit(Resource.Error(UIText.StringResource(R.string.em_password_blank)))
                return@flow
            }

            password.length < MINIMUM_PASSWORD_LENGTH -> {
                emit(Resource.Error(UIText.StringResource(R.string.em_register_password_too_short)))
                return@flow
            }

            repeatedPassword.isBlank() -> {
                emit(Resource.Error(UIText.StringResource(R.string.em_repeat_password_blank)))
                return@flow
            }

            !password.contentEquals(repeatedPassword) -> {
                emit(Resource.Error(UIText.StringResource(R.string.em_password_not_match)))
                return@flow
            }
        }

        try {
            authApi
                .registerWithEmail(email = email, password = password)
                .also { result ->
                    when (result) {
                        is Resource.Success -> {
                            emit(authApi.updateProfile(newDisplayName = name, newPhotoUri = null))
                        }
                        else -> emit(result)
                    }
                }

        } catch (e: Exception) {
            emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
        }
    }

    override fun sendPasswordResetEmail(
        email: String,
    ): Flow<SimpleResource> = flow {
        emit(Resource.Loading())

        when {
            email.isBlank() -> {
                emit(Resource.Error(UIText.StringResource(R.string.em_email_blank)))
                return@flow
            }

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                emit(Resource.Error(UIText.StringResource(R.string.em_invalid_email)))
                return@flow
            }
        }

        try {
            authApi
                .sendPasswordResetEmail(email = email)
                .also { emit(it) }
        } catch (e: Exception) {
            emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
        }
    }

    override fun logout() = authApi.logout()

    companion object {
        private const val MINIMUM_PASSWORD_LENGTH = 6
    }
}
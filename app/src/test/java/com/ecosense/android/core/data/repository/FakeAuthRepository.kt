package com.ecosense.android.core.data.repository

import com.ecosense.android.core.domain.model.User
import com.ecosense.android.core.domain.repository.AuthRepository
import com.ecosense.android.core.util.SimpleResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeAuthRepository(
    private var fakeIsLoggedIn: Boolean? = null,
    private val fakeCurrentUser: User? = null,
    private val fakeIdToken: String? = null,
    private val fakeLoginWithEmailResult: SimpleResource? = null,
    private val fakeLoginWithGoogleResult: SimpleResource? = null,
    private val fakeRegisterWithEmailResult: SimpleResource? = null,
    private val fakeSendPasswordResetEmailResult: SimpleResource? = null,
    private val fakeUpdateProfileResult: SimpleResource? = null,
    private val fakeSendEmailVerificationResult: SimpleResource? = null,
) : AuthRepository {

    override val isLoggedIn: Flow<Boolean>
        get() = flow {
            emit(fakeIsLoggedIn ?: throw NullPointerException())
        }

    override suspend fun getCurrentUser(): User? = fakeCurrentUser

    override fun loginWithEmail(email: String, password: String): Flow<SimpleResource> = flow {
        emit(fakeLoginWithEmailResult ?: throw NullPointerException())
    }

    override fun loginWithGoogle(idToken: String?): Flow<SimpleResource> = flow {
        emit(fakeLoginWithGoogleResult ?: throw NullPointerException())
    }

    override fun registerWithEmail(
        name: String,
        email: String,
        password: String,
        repeatedPassword: String
    ): Flow<SimpleResource> = flow {
        emit(fakeRegisterWithEmailResult ?: throw NullPointerException())
    }

    override fun sendPasswordResetEmail(email: String): Flow<SimpleResource> = flow {
        emit(fakeSendPasswordResetEmailResult ?: throw NullPointerException())
    }

    override fun logout() {
        fakeIsLoggedIn = false
    }
}
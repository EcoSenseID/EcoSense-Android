package com.ecosense.android.core.domain.api

import android.net.Uri
import com.ecosense.android.core.domain.model.User
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.SimpleResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeAuthApi(
    private var fakeIsLoggedIn: Boolean? = null,
    private val fakeCurrentUser: User? = null,
    private val fakeIdToken: String? = null,
    private val fakeLoginWithEmailResult: SimpleResource? = null,
    private val fakeLoginWithGoogleResult: SimpleResource? = null,
    private val fakeRegisterWithEmailResult: SimpleResource? = null,
    private val fakeSendPasswordResetEmailResult: SimpleResource? = null,
    private val fakeUpdateProfileResult: SimpleResource? = null,
    private val fakeSendEmailVerificationResult: SimpleResource? = null,
) : AuthApi {

    override val isLoggedIn: Flow<Boolean>
        get() = flow {
            emit(fakeIsLoggedIn ?: throw NullPointerException())
        }

    override suspend fun getCurrentUser(): User? = fakeCurrentUser

    override suspend fun getIdToken(forceRefresh: Boolean): String? = fakeIdToken

    override suspend fun loginWithEmail(
        email: String,
        password: String
    ): SimpleResource = fakeLoginWithEmailResult ?: throw NullPointerException()

    override suspend fun loginWithGoogle(
        idToken: String?
    ): SimpleResource = fakeLoginWithGoogleResult ?: throw NullPointerException()

    override suspend fun registerWithEmail(
        email: String,
        password: String
    ): SimpleResource = fakeRegisterWithEmailResult ?: Resource.Success(Unit)

    override suspend fun sendPasswordResetEmail(
        email: String
    ): SimpleResource = fakeSendPasswordResetEmailResult ?: throw NullPointerException()

    override fun logout() {
        fakeIsLoggedIn = false
    }

    override suspend fun updateProfile(
        newDisplayName: String?,
        newPhotoUri: Uri?
    ): SimpleResource = fakeUpdateProfileResult ?: throw NullPointerException()

    override suspend fun sendEmailVerification(): SimpleResource =
        fakeSendEmailVerificationResult ?: throw NullPointerException()
}
package com.ecosense.android.core.domain.api

import android.net.Uri
import com.ecosense.android.core.domain.model.User
import com.ecosense.android.core.util.SimpleResource
import kotlinx.coroutines.flow.Flow
import org.junit.Assert.*

import org.junit.Test

class FakeAuthApi: AuthApi {
    override val isLoggedIn: Flow<Boolean>
        get() = TODO("Not yet implemented")

    override suspend fun getCurrentUser(): User? {
        TODO("Not yet implemented")
    }

    override suspend fun getIdToken(forceRefresh: Boolean): String? {
        TODO("Not yet implemented")
    }

    override suspend fun loginWithEmail(email: String, password: String): SimpleResource {
        TODO("Not yet implemented")
    }

    override suspend fun loginWithGoogle(idToken: String?): SimpleResource {
        TODO("Not yet implemented")
    }

    override suspend fun registerWithEmail(email: String, password: String): SimpleResource {
        TODO("Not yet implemented")
    }

    override suspend fun sendPasswordResetEmail(email: String): SimpleResource {
        TODO("Not yet implemented")
    }

    override fun logout() {
        TODO("Not yet implemented")
    }

    override suspend fun updateProfile(newDisplayName: String?, newPhotoUri: Uri?): SimpleResource {
        TODO("Not yet implemented")
    }

    override suspend fun sendEmailVerification(): SimpleResource {
        TODO("Not yet implemented")
    }

}
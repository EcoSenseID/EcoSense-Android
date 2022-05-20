package com.ecosense.android.featAuth.domain.usecase

import com.ecosense.android.R
import com.ecosense.android.core.domain.repository.AuthRepository
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.SimpleResource
import com.ecosense.android.core.util.UIText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GoogleSignInUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(idToken: String?): Flow<SimpleResource> {
        return when {
            idToken.isNullOrBlank() -> flow {
                emit(Resource.Error(UIText.StringResource(R.string.login_failed)))
            }
            else -> authRepository.loginWithGoogle(idToken = idToken)
        }
    }
}
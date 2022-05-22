package com.ecosense.android.featAuth.presentation.login

data class LoginScreenState(
    val email: String,
    val password: String,
    val isLoadingEmailLogin: Boolean,
    val isLoadingGoogleLogin: Boolean,
    val isPasswordVisible: Boolean,
) {
    val isLoading = isLoadingEmailLogin || isLoadingGoogleLogin

    companion object {
        val defaultValue = LoginScreenState(
            email = "",
            password = "",
            isLoadingEmailLogin = false,
            isLoadingGoogleLogin = false,
            isPasswordVisible = false,
        )
    }
}
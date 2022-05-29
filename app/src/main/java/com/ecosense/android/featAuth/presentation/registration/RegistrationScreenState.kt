package com.ecosense.android.featAuth.presentation.registration

data class RegistrationScreenState(
    val name: String,
    val email: String,
    val password: String,
    val isPasswordVisible: Boolean,
    val repeatedPassword: String,
    val isRepeatedPasswordVisible: Boolean,
    val isRegistering: Boolean,
    val isLoadingGoogleLogin: Boolean,
) {
    val isLoading = isRegistering || isLoadingGoogleLogin

    companion object {
        val defaultValue = RegistrationScreenState(
            name = "",
            email = "",
            password = "",
            isPasswordVisible = false,
            repeatedPassword = "",
            isRepeatedPasswordVisible = false,
            isRegistering = false,
            isLoadingGoogleLogin = false,
        )
    }
}
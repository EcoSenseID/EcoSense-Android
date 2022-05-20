package com.ecosense.android.featAuth.presentation.registration

data class RegistrationScreenState(
    val email: String,
    val password: String,
    val isPasswordVisible: Boolean,
    val repeatedPassword: String,
    val isRepeatedPasswordVisible: Boolean,
    val isLoading: Boolean,
) {
    companion object {
        val defaultValue = RegistrationScreenState(
            email = "",
            password = "",
            isPasswordVisible = false,
            repeatedPassword = "",
            isRepeatedPasswordVisible = false,
            isLoading = false,
        )
    }
}
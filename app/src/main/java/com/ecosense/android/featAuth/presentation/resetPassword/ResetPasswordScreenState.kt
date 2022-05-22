package com.ecosense.android.featAuth.presentation.resetPassword

data class ResetPasswordScreenState(
    val email: String,
    val isLoading: Boolean,
    val isSuccessful: Boolean,
) {
    companion object {
        val defaultValue = ResetPasswordScreenState(
            email = "",
            isLoading = false,
            isSuccessful = false,
        )
    }
}
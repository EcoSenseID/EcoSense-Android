package com.ecosense.android.featProfile.presentation.editProfile

data class EditProfileScreenState(
    val uid: String?,
    val displayName: String?,
    val email: String?,
    val photoUrl: String?,
    val isSavingProfileLoading: Boolean,
    val isEmailVerified: Boolean?,
    val isEmailVerificationLoading: Boolean,
) {
    companion object {
        val defaultValue = EditProfileScreenState(
            uid = null,
            displayName = null,
            email = null,
            photoUrl = null,
            isSavingProfileLoading = false,
            isEmailVerified = null,
            isEmailVerificationLoading = false
        )
    }
}
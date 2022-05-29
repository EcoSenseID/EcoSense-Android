package com.ecosense.android.core.domain.model

data class User(
    val uid: String?,
    val displayName: String?,
    val email: String?,
    val photoUrl: String?,
    val isEmailVerified: Boolean?,
) {
    companion object {
        val defaultValue = User(
            uid = null,
            displayName = null,
            email = null,
            photoUrl = null,
            isEmailVerified = null,
        )
    }
}
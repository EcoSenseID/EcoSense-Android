package com.ecosense.android.core.domain.model

data class User(
    val displayName: String?,
    val email: String?,
    val photoUrl: String?,
    val phoneNumber: String?,
) {
    companion object {
        val defaultValue = User(
            email = null,
            displayName = null,
            photoUrl = null,
            phoneNumber = null,
        )
    }
}
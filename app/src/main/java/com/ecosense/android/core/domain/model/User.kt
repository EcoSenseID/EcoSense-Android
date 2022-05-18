package com.ecosense.android.core.domain.model

data class User(
    val email: String?
) {
    companion object {
        val defaultValue = User(
            email = null
        )
    }
}
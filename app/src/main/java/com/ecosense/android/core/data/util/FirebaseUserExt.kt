package com.ecosense.android.core.data.util

import com.ecosense.android.core.domain.model.User
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toUser() = User(
    uid = this.uid,
    displayName = this.displayName,
    email = this.email,
    photoUrl = this.photoUrl?.toString(),
    isEmailVerified = this.isEmailVerified
)
package com.ecosense.android.core.data.util

import com.ecosense.android.core.domain.model.User
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toUser() = User(
    email = this.email
)
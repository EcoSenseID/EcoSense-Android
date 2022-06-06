package com.ecosense.android.util

import android.net.Uri
import com.ecosense.android.core.domain.model.User
import java.util.*
import kotlin.random.Random

object TestFaker {
    val validEmail get() = "email${System.currentTimeMillis()}@gmail.com"

    val invalidEmail get() = "1234567890"

    val validPassword get() = "password${System.currentTimeMillis()}"

    val validName get() = "John Doe"

    val validPhotoUri get() = Uri.parse(validPhotoUrl)

    val validPhotoUrl
        get() = "https://cdn.statically.io/og/theme=dark/${System.currentTimeMillis()}.jpg"

    val validIdToken get() = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"

    val user
        get() = User(
            uid = UUID.randomUUID().toString(),
            displayName = validName,
            email = validEmail,
            photoUrl = validPhotoUrl,
            isEmailVerified = Random.nextBoolean(),
        )

    fun getLorem(length: Int = 20): String {
        var lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit sed do eiusmod tempor."

        while (lorem.length < length) {
            lorem += lorem
        }

        return lorem.take(length)
    }
}
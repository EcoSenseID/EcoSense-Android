package com.ecosense.android.core.domain.api

import android.net.Uri

interface CloudStorageApi {
    suspend fun uploadProfilePicture(
        photoUri: Uri,
        uid: String
    ): Uri?
}
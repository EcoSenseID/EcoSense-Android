package com.ecosense.android.core.domain.api

import android.net.Uri

class FakeCloudStorageApi(
    private val uploadProfilePictureResult: Uri? = null
) : CloudStorageApi {
    override suspend fun uploadProfilePicture(photoUri: Uri, uid: String): Uri? {
        return null
    }
}
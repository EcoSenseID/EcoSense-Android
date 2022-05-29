package com.ecosense.android.core.data.api

import android.net.Uri
import com.ecosense.android.core.domain.api.CloudStorageApi
import com.google.firebase.storage.FirebaseStorage
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseStorageApi : CloudStorageApi {
    private val firebaseStorage = FirebaseStorage.getInstance()

    override suspend fun uploadProfilePicture(
        photoUri: Uri,
        uid: String,
    ): Uri? = suspendCoroutine { cont ->
        val ref = firebaseStorage
            .reference
            .child("users/$uid/profile")

        ref
            .putFile(photoUri)
            .continueWithTask { task ->
                if (!task.isSuccessful) task.exception?.let { throw it }
                ref.downloadUrl
            }
            .addOnCompleteListener { task ->
                when {
                    task.isSuccessful -> cont.resume(task.result)
                    else -> cont.resume(null)
                }
            }
    }
}
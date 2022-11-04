package com.ecosense.android.featForums.data.repository

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.ecosense.android.R
import com.ecosense.android.core.domain.api.AuthApi
import com.ecosense.android.core.domain.model.Story
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.SimpleResource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featForums.data.api.ForumsApi
import com.ecosense.android.featForums.domain.model.Reply
import com.ecosense.android.featForums.domain.model.Supporter
import com.ecosense.android.featForums.domain.repository.ForumsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import logcat.asLog
import logcat.logcat
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class ForumsRepositoryImpl(
    private val appContext: Context,
    private val authApi: AuthApi,
    private val forumsApi: ForumsApi,
) : ForumsRepository {

    override suspend fun getStories(
        page: Int,
        size: Int,
    ): Resource<List<Story>> {
        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            logcat { "getStories. page: $page, size: $size" }
            val response = forumsApi.getStories(
                bearerToken = bearerToken,
                page = page,
                size = size,
            )

            return when {
                response.error == true -> Resource.Error(uiText = response.message?.let {
                    UIText.DynamicString(it)
                } ?: UIText.StringResource(R.string.em_unknown))

                response.stories == null -> {
                    Resource.Error(UIText.StringResource(R.string.em_unknown))
                }

                else -> Resource.Success(response.stories.map { it.toDomain() })
            }

        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is IOException -> UIText.StringResource(R.string.em_io_exception)
                else -> UIText.StringResource(R.string.em_unknown)
            }.let { return Resource.Error(it) }
        }
    }

    override suspend fun getStoryReplies(
        storyId: Int,
        page: Int,
        size: Int,
    ): Resource<List<Reply>> {
        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            val response = forumsApi.getStoryReplies(
                bearerToken = bearerToken,
                storyId = storyId,
                page = page,
                size = size,
            )

            when {
                response.error == true -> return Resource.Error(uiText = response.message?.let {
                    UIText.DynamicString(it)
                } ?: UIText.StringResource(R.string.em_unknown))

                response.replies == null -> {
                    return Resource.Error(UIText.StringResource(R.string.em_unknown))
                }

                else -> return Resource.Success(response.replies.map { it.toDomain() })
            }

        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is IOException -> UIText.StringResource(R.string.em_io_exception)
                else -> UIText.StringResource(R.string.em_unknown)
            }.let { return Resource.Error(it) }
        }
    }

    override suspend fun getStorySupporters(
        storyId: Int,
        page: Int,
        size: Int,
    ): Resource<List<Supporter>> {
        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            val response = forumsApi.getStorySupporters(
                bearerToken = bearerToken,
                storyId = storyId,
                page = page,
                size = size,
            )

            when {
                response.error == true -> return Resource.Error(uiText = response.message?.let {
                    UIText.DynamicString(it)
                } ?: UIText.StringResource(R.string.em_unknown))

                response.supporters == null -> {
                    return Resource.Error(UIText.StringResource(R.string.em_unknown))
                }

                else -> return Resource.Success(response.supporters.map { it.toDomain() })
            }

        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is IOException -> UIText.StringResource(R.string.em_io_exception)
                else -> UIText.StringResource(R.string.em_unknown)
            }.let { return Resource.Error(it) }
        }
    }

    override fun postNewStory(
        caption: String,
        attachedPhoto: File?,
        sharedCampaignId: Int?,
    ): Flow<SimpleResource> = flow {
        emit(Resource.Loading())

        if (caption.isBlank()) {
            emit(Resource.Error(UIText.StringResource(R.string.em_caption_blank)))
            return@flow
        }

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"

            val rbCaption = caption.toRequestBody("text/plain".toMediaType())

            val rbSharedCampaignId =
                sharedCampaignId?.toString()?.toRequestBody("text/plain".toMediaType())

            val attachedPhotoMultipart: MultipartBody.Part? = attachedPhoto?.let {
                val rbAttachedPhoto = try {
                    compressJpeg(it)
                } catch (e: Exception) {
                    it // abort compression
                }.asRequestBody("image/jpeg".toMediaTypeOrNull())

                MultipartBody.Part.createFormData(
                    name = "attachedPhoto",
                    filename = attachedPhoto.name,
                    body = rbAttachedPhoto,
                )
            }

            val response = forumsApi.postNewStory(
                bearerToken = bearerToken,
                caption = rbCaption,
                sharedCampaignId = rbSharedCampaignId,
                attachedPhoto = attachedPhotoMultipart,
            )

            when {
                response.error != true -> emit(Resource.Success(Unit))
                response.message != null -> emit(
                    Resource.Error(UIText.DynamicString(response.message))
                )
                else -> emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
            }
        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is IOException -> UIText.StringResource(R.string.em_io_exception)
                else -> UIText.StringResource(R.string.em_unknown)
            }.let { emit(Resource.Error(it)) }
        }
    }

    override fun postNewReply(
        storyId: Int,
        caption: String,
        attachedPhoto: File?,
    ): Flow<SimpleResource> = flow {
        emit(Resource.Loading())

        if (caption.isBlank()) {
            emit(Resource.Error(UIText.StringResource(R.string.em_caption_blank)))
            return@flow
        }

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"

            val rbStoryId = storyId.toString().toRequestBody("text/plain".toMediaType())
            val rbCaption = caption.toRequestBody("text/plain".toMediaType())

            val attachedPhotoMultipart: MultipartBody.Part? = attachedPhoto?.let {
                val rbAttachedPhoto = try {
                    compressJpeg(it)
                } catch (e: Exception) {
                    it // abort compression
                }.asRequestBody("image/jpeg".toMediaTypeOrNull())

                MultipartBody.Part.createFormData(
                    name = "attachedPhoto",
                    filename = attachedPhoto.name,
                    body = rbAttachedPhoto,
                )
            }

            val response = forumsApi.postNewReply(
                bearerToken = bearerToken,
                storyId = rbStoryId,
                caption = rbCaption,
                attachedPhoto = attachedPhotoMultipart,
            )

            when {
                response.error != true -> emit(Resource.Success(Unit))
                response.message != null -> emit(
                    Resource.Error(UIText.DynamicString(response.message))
                )
                else -> emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
            }
        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is IOException -> UIText.StringResource(R.string.em_io_exception)
                else -> UIText.StringResource(R.string.em_unknown)
            }.let { emit(Resource.Error(it)) }
        }
    }

    override fun postSupportStory(
        storyId: Int,
    ): Flow<SimpleResource> = flow {
        emit(Resource.Loading())

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"

            val response = forumsApi.postSupportStory(
                bearerToken = bearerToken,
                storyId = storyId,
            )

            when {
                response.error == true -> emit(
                    Resource.Error(
                        if (response.message != null) UIText.DynamicString(response.message)
                        else UIText.StringResource(R.string.em_unknown)
                    )
                )

                else -> emit(Resource.Success(Unit))
            }

        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is IOException -> UIText.StringResource(R.string.em_io_exception)
                else -> UIText.StringResource(R.string.em_unknown)
            }.let { emit(Resource.Error(it)) }
        }
    }

    override fun postUnsupportStory(
        storyId: Int,
    ): Flow<SimpleResource> = flow {
        emit(Resource.Loading())

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"

            val response = forumsApi.postUnsupportStory(
                bearerToken = bearerToken,
                storyId = storyId,
            )

            when {
                response.error == true -> emit(
                    Resource.Error(
                        if (response.message != null) UIText.DynamicString(response.message)
                        else UIText.StringResource(R.string.em_unknown)
                    )
                )

                else -> emit(Resource.Success(Unit))
            }

        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is IOException -> UIText.StringResource(R.string.em_io_exception)
                else -> UIText.StringResource(R.string.em_unknown)
            }.let { emit(Resource.Error(it)) }
        }
    }

    override fun postSupportReply(
        replyId: Int,
    ): Flow<SimpleResource> = flow {
        emit(Resource.Loading())

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"

            val response = forumsApi.postSupportReply(
                bearerToken = bearerToken,
                replyId = replyId,
            )

            when {
                response.error == true -> emit(
                    Resource.Error(
                        if (response.message != null) UIText.DynamicString(response.message)
                        else UIText.StringResource(R.string.em_unknown)
                    )
                )

                else -> emit(Resource.Success(Unit))
            }

        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is IOException -> UIText.StringResource(R.string.em_io_exception)
                else -> UIText.StringResource(R.string.em_unknown)
            }.let { emit(Resource.Error(it)) }
        }
    }

    override fun postUnsupportReply(
        replyId: Int,
    ): Flow<SimpleResource> = flow {
        emit(Resource.Loading())

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"

            val response = forumsApi.postUnsupportReply(
                bearerToken = bearerToken,
                replyId = replyId,
            )

            when {
                response.error == true -> emit(
                    Resource.Error(
                        if (response.message != null) UIText.DynamicString(response.message)
                        else UIText.StringResource(R.string.em_unknown)
                    )
                )

                else -> emit(Resource.Success(Unit))
            }

        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is IOException -> UIText.StringResource(R.string.em_io_exception)
                else -> UIText.StringResource(R.string.em_unknown)
            }.let { emit(Resource.Error(it)) }
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun compressJpeg(file: File): File = withContext(Dispatchers.IO) {
        val bitmap = BitmapFactory.decodeFile(file.path)

        var compressQuality = 100
        var streamLength: Int

        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)

        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))

        return@withContext file
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun getNewTempJpeg(): File = withContext(Dispatchers.IO) {
        val sdf = SimpleDateFormat("dd-MMM-yyyy", Locale.US)
        val timeStamp: String = sdf.format(System.currentTimeMillis())

        val dirPictures: File? = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return@withContext File.createTempFile(timeStamp, ".jpeg", dirPictures)
    }

    override suspend fun getNewTempJpegUri(): Uri = withContext(Dispatchers.IO) {
        val tempJpeg = getNewTempJpeg()
        return@withContext FileProvider.getUriForFile(
            appContext, appContext.packageName, tempJpeg
        )
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun findJpegByUri(uri: Uri): File? = withContext(Dispatchers.IO) {
        try {
            val contentResolver: ContentResolver = appContext.contentResolver
            val tempJpeg = getNewTempJpeg()

            val inputStream: InputStream =
                contentResolver.openInputStream(uri) ?: return@withContext null
            val outputStream: OutputStream = FileOutputStream(tempJpeg)

            val buf = ByteArray(1024)
            var len: Int
            while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)

            outputStream.close()
            inputStream.close()

            return@withContext tempJpeg
        } catch (e: Exception) {
            return@withContext null
        }
    }
}
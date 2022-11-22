package com.ecosense.android.featDiscoverCampaign.data.repository

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.ecosense.android.R
import com.ecosense.android.core.domain.api.AuthApi
import com.ecosense.android.core.domain.model.Campaign
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.SimpleResource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featDiscoverCampaign.data.api.DiscoverApi
import com.ecosense.android.featDiscoverCampaign.data.model.*
import com.ecosense.android.featDiscoverCampaign.domain.model.CampaignDetail
import com.ecosense.android.featDiscoverCampaign.domain.model.Category
import com.ecosense.android.featDiscoverCampaign.domain.model.Dashboard
import com.ecosense.android.featDiscoverCampaign.domain.repository.DiscoverCampaignRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
import retrofit2.HttpException
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class DiscoverCampaignRepositoryImpl(
    private val authApi: AuthApi,
    private val discoverApi: DiscoverApi,
    private val appContext: Context
) : DiscoverCampaignRepository {
    override fun getCampaigns(q: String?, categoryId: Int?): Flow<Resource<List<Campaign>>> = flow {
        emit(Resource.Loading())

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            val response =
                discoverApi.getCampaigns(bearerToken = bearerToken, q = q, categoryId = categoryId)

            when {
                response.error == true -> emit(Resource.Error(uiText = response.message?.let {
                    UIText.DynamicString(
                        it
                    )
                } ?: UIText.StringResource(R.string.em_unknown)))

                response.campaigns == null -> {
                    emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
                }

                else -> {
                    emit(Resource.Success(response.campaigns.map { it.toCampaign() }))
                }
            }

        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<BrowseCampaignDto>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<BrowseCampaignDto>() {}.type
                        )
                        UIText.DynamicString(response.message!!)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.em_unknown)
                    }
                }

                is IOException -> UIText.StringResource(R.string.em_io_exception)

                else -> UIText.StringResource(R.string.em_unknown)

            }.let { emit(Resource.Error(it)) }
        }
    }

    override fun getCampaignDetail(
        id: Int,
        recordId: Int?,
    ): Flow<Resource<CampaignDetail>> = flow {
        emit(Resource.Loading())

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            val response = if (recordId != null) discoverApi.getCampaignDetail(
                bearerToken = bearerToken,
                id = id,
                recordId = recordId,
            ) else discoverApi.getCampaignDetail(
                bearerToken = bearerToken,
                id = id,
            )


            when (response.error) {
                true -> emit(Resource.Error(uiText = response.message?.let { UIText.DynamicString(it) }
                    ?: UIText.StringResource(R.string.em_unknown)))
                else -> emit(Resource.Success(response.toCampaignDetails()))
            }

        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<CampaignDetailDto>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<CampaignDetailDto>() {}.type
                        )
                        UIText.DynamicString(response.message!!)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.em_unknown)
                    }
                }

                is IOException -> UIText.StringResource(R.string.em_io_exception)

                else -> UIText.StringResource(R.string.em_unknown)

            }.let { emit(Resource.Error(it)) }
        }
    }

    override fun getCategories(): Flow<Resource<List<Category>>> = flow {
        emit(Resource.Loading())

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            val response = discoverApi.getCategories(bearerToken = bearerToken)

            when {
                response.error == true -> emit(Resource.Error(uiText = response.message?.let {
                    UIText.DynamicString(
                        it
                    )
                } ?: UIText.StringResource(R.string.em_unknown)))

                response.categories == null -> {
                    emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
                }

                else -> {
                    emit(Resource.Success(response.categories.map { it.toCategories() }))
                }
            }

        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<CategoriesDto>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<CategoriesDto>() {}.type
                        )
                        UIText.DynamicString(response.message!!)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.em_unknown)
                    }
                }

                is IOException -> UIText.StringResource(R.string.em_io_exception)

                else -> UIText.StringResource(R.string.em_unknown)

            }.let { emit(Resource.Error(it)) }
        }
    }

    override fun getDashboard(): Flow<Resource<Dashboard>> = flow {
        emit(Resource.Loading())

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            val response = discoverApi.getDashboard(bearerToken = bearerToken)

            when {
                response.error == true -> emit(Resource.Error(uiText = response.message?.let {
                    UIText.DynamicString(
                        it
                    )
                } ?: UIText.StringResource(R.string.em_unknown)))

                response.campaigns == null || response.categories == null -> {
                    emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
                }

                else -> emit(
                    Resource.Success(
                        Dashboard(campaigns = response.campaigns.map { it.toDashboardCampaign() },
                            categories = response.categories.map { it.toCategories() })
                    )
                )
            }

        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<DashboardDto>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<DashboardDto>() {}.type
                        )
                        UIText.DynamicString(response.message!!)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.em_unknown)
                    }
                }

                is IOException -> UIText.StringResource(R.string.em_io_exception)

                else -> UIText.StringResource(R.string.em_unknown)

            }.let { emit(Resource.Error(it)) }
        }
    }

    override fun setCompletionProof(
        photo: String?, caption: String?, missionId: Int
    ): Flow<SimpleResource> = flow {
        emit(Resource.Loading())

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            val captionRB = caption?.toRequestBody("text/plain".toMediaType())
            var photoMultipart: MultipartBody.Part? = null

            if (photo != null) {
                val photoUri = Uri.parse(photo)

                val photoFile = uriToFile(photoUri, appContext)

                val reducedPhotoFile = reduceFileImage(photoFile)

                val requestPhotoFile =
                    reducedPhotoFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                photoMultipart = requestPhotoFile.let {
                    MultipartBody.Part.createFormData(
                        "photo", reducedPhotoFile.name, it
                    )
                }
            }

            val response = discoverApi.setCompletionProof(
                bearerToken = bearerToken,
                photo = photoMultipart,
                caption = captionRB,
                missionId = missionId
            )

            when {
                response.error == false -> emit(Resource.Success(Unit))

                response.message != null -> emit(Resource.Error(UIText.DynamicString(response.message)))

                else -> emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
            }

        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<CompletionProofDto>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<CompletionProofDto>() {}.type
                        )
                        UIText.DynamicString(response.message!!)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.em_unknown)
                    }
                }

                is IOException -> UIText.StringResource(R.string.em_io_exception)

                else -> UIText.StringResource(R.string.em_unknown)

            }.let { emit(Resource.Error(it)) }
        }
    }

    override fun setJoinCampaign(campaignId: Int): Flow<SimpleResource> = flow {
        emit(Resource.Loading())

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"

            val response =
                discoverApi.setJoinCampaign(bearerToken = bearerToken, campaignId = campaignId)

            when {
                response.error == false -> emit(Resource.Success(Unit))

                response.message != null -> emit(Resource.Error(UIText.DynamicString(response.message)))

                else -> emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
            }

        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<JoinCampaignDto>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<JoinCampaignDto>() {}.type
                        )
                        UIText.DynamicString(response.message!!)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.em_unknown)
                    }
                }

                is IOException -> UIText.StringResource(R.string.em_io_exception)

                else -> UIText.StringResource(R.string.em_unknown)

            }.let { emit(Resource.Error(it)) }
        }
    }

    override fun setCompleteCampaign(campaignId: Int): Flow<SimpleResource> = flow {
        emit(Resource.Loading())

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"

            val response =
                discoverApi.setCompleteCampaign(bearerToken = bearerToken, campaignId = campaignId)

            when {
                response.error == false -> emit(Resource.Success(Unit))

                response.message != null -> emit(Resource.Error(UIText.DynamicString(response.message)))

                else -> emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
            }

        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<CompleteCampaignDto>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<CompleteCampaignDto>() {}.type
                        )
                        UIText.DynamicString(response.message!!)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.em_unknown)
                    }
                }

                is IOException -> UIText.StringResource(R.string.em_io_exception)

                else -> UIText.StringResource(R.string.em_unknown)

            }.let { emit(Resource.Error(it)) }
        }
    }

    override suspend fun getNewTempJpegUri(): Uri = withContext(Dispatchers.IO) {
        val tempJpeg = getNewTempJpeg()
        return@withContext FileProvider.getUriForFile(
            appContext, appContext.packageName, tempJpeg
        )
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun getNewTempJpeg(): File = withContext(Dispatchers.IO) {
        val sdf = SimpleDateFormat("dd-MMM-yyyy", Locale.US)
        val timeStamp: String = sdf.format(System.currentTimeMillis())

        val dirPictures: File? = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return@withContext File.createTempFile(timeStamp, ".jpeg", dirPictures)
    }


    private fun createCustomTempFile(context: Context): File {
        val filenameFormat = "dd-MMM-yyyy"

        val timeStamp: String = SimpleDateFormat(
            filenameFormat, Locale.US
        ).format(System.currentTimeMillis())

        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    private fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    private fun reduceFileImage(file: File): File {
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

        return file
    }
}
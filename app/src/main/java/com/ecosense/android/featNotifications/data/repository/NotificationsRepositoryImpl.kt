package com.ecosense.android.featNotifications.data.repository

import com.ecosense.android.R
import com.ecosense.android.core.domain.api.AuthApi
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featNotifications.data.api.NotificationsApi
import com.ecosense.android.featNotifications.domain.model.Notification
import com.ecosense.android.featNotifications.domain.repository.NotificationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import logcat.asLog
import logcat.logcat
import java.io.IOException
import java.util.*

class NotificationsRepositoryImpl(
    private val authApi: AuthApi,
    private val notificationsApi: NotificationsApi,
) : NotificationsRepository {

    override fun getNotifications(): Flow<Resource<List<Notification>>> = flow {
        emit(Resource.Loading())

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            val response = notificationsApi.getNotifications(
                bearerToken = bearerToken,
                language = Locale.getDefault().language,
            )

            when {
                response.error == true -> emit(Resource.Error(uiText = response.message?.let {
                    UIText.DynamicString(it)
                } ?: UIText.StringResource(R.string.em_unknown)))

                response.notifications == null -> {
                    emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
                }

                else -> emit(Resource.Success(response.notifications.map { it.toDomain() }))
            }

        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is IOException -> UIText.StringResource(R.string.em_io_exception)
                else -> UIText.StringResource(R.string.em_unknown)
            }.let { emit(Resource.Error(it)) }
        }
    }

}
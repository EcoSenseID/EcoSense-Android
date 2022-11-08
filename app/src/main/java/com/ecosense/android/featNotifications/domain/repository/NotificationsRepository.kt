package com.ecosense.android.featNotifications.domain.repository

import com.ecosense.android.core.util.Resource
import com.ecosense.android.featNotifications.domain.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationsRepository {
    fun getNotifications(): Flow<Resource<List<Notification>>>
}
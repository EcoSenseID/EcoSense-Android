package com.ecosense.android.featForums.domain.repository

import com.ecosense.android.core.util.Resource
import com.ecosense.android.featForums.domain.model.Story

interface ForumsRepository {

    suspend fun getStories(
        page: Int,
        size: Int,
    ): Resource<List<Story>>

}
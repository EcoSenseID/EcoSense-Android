package com.ecosense.android.core.domain.model

data class Campaign(
    val id: Int,
    val posterUrl: String,
    val title: String,
    val endDate: String,
    val category: List<String>,
    val participantsCount: Int,
    val isTrending: Boolean,
    val isNew: Boolean
)
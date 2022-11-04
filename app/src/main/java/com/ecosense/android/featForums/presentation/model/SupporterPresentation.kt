package com.ecosense.android.featForums.presentation.model

import com.ecosense.android.featForums.domain.model.Supporter

data class SupporterPresentation(
    val id: Int,
    val avatarUrl: String,
    val name: String,
)

fun Supporter.toPresentation() = SupporterPresentation(
    id = this.id,
    avatarUrl = this.avatarUrl,
    name = this.name,
)

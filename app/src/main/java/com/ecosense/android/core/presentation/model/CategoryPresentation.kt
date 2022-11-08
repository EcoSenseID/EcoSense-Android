package com.ecosense.android.core.presentation.model

import android.os.Parcelable
import com.ecosense.android.core.domain.model.Category
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryPresentation(
    val name: String,
    val colorHex: String,
) : Parcelable

fun Category.toPresentation(): CategoryPresentation = CategoryPresentation(
    name = this.name,
    colorHex = this.colorHex,
)
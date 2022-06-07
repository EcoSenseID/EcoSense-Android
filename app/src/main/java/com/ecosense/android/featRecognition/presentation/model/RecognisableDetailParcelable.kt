package com.ecosense.android.featRecognition.presentation.model

import android.os.Parcelable
import com.ecosense.android.featRecognition.domain.model.Recognisable
import com.ecosense.android.featRecognition.domain.model.RecognisableDetail
import com.ecosense.android.featRecognition.domain.model.SavedRecognisable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecognisableDetailParcelable(
    val id: Int?,
    val savedAt: Int,
    val confidencePercent: Int,
    val label: String,
) : Parcelable {
    fun toRecognisableDetail() = RecognisableDetail(
        id = this.id,
        label = this.label,
        savedAt = this.savedAt,
        confidencePercent = this.confidencePercent,
    )
}

fun SavedRecognisable.toDetailParcelable() = RecognisableDetailParcelable(
    id = this.id,
    savedAt = this.savedAt,
    confidencePercent = this.confidencePercent,
    label = this.label,
)

fun Recognisable.toDetailParcelable(
    savedAt: Int = (System.currentTimeMillis() / 1000).toInt(),
) = RecognisableDetailParcelable(
    id = null,
    savedAt = savedAt,
    confidencePercent = this.confidencePercent,
    label = this.label,
)

fun RecognisableDetail.toParcelable() = RecognisableDetailParcelable(
    id = this.id,
    savedAt = this.savedAt,
    confidencePercent = this.confidencePercent,
    label = this.label
)
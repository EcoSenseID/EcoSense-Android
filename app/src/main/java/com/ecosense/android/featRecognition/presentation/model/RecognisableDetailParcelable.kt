package com.ecosense.android.featRecognition.presentation.model

import android.os.Parcelable
import com.ecosense.android.featRecognition.domain.model.Recognisable
import com.ecosense.android.featRecognition.domain.model.RecognisableDetail
import com.ecosense.android.featRecognition.domain.model.SavedRecognisable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecognisableDetailParcelable(
    val id: Int?,
    val timeInMillis: Long,
    val confidencePercent: Int,
    val label: String,
) : Parcelable {
    fun toRecognisableDetail() = RecognisableDetail(
        id = this.id,
        label = this.label,
        timeInMillis = this.timeInMillis,
        confidencePercent = this.confidencePercent,
    )
}

fun SavedRecognisable.toDetailParcelable() = RecognisableDetailParcelable(
    id = this.id,
    timeInMillis = this.timeInMillis,
    confidencePercent = this.confidencePercent,
    label = this.label,
)

fun Recognisable.toDetailParcelable(
    timeInMillis: Long = System.currentTimeMillis(),
) = RecognisableDetailParcelable(
    id = null,
    timeInMillis = timeInMillis,
    confidencePercent = this.confidencePercent,
    label = this.label,
)

fun RecognisableDetail.toParcelable() = RecognisableDetailParcelable(
    id = this.id,
    timeInMillis = this.timeInMillis,
    confidencePercent = this.confidencePercent,
    label = this.label
)
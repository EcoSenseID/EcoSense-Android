package com.ecosense.android.featRecognition.data.util

import com.ecosense.android.core.data.model.SavedRecognisableEntity
import com.ecosense.android.featRecognition.data.source.DiseaseDataSource
import com.ecosense.android.featRecognition.domain.model.Recognisable
import com.ecosense.android.featRecognition.domain.model.SavedRecognisable
import org.tensorflow.lite.support.label.Category

fun Category.toRecognisable(): Recognisable {
    val disease = DiseaseDataSource.getDisease(this.label)

    return Recognisable(
        label = this.label,
        confidencePercent = (this.score * 100).toInt(),
        readableName = disease?.readableName,
    )
}

fun SavedRecognisableEntity.toSavedRecognisable(): SavedRecognisable {
    val disease = DiseaseDataSource.getDisease(this.label)

    return SavedRecognisable(
        id = this.id,
        timeInMillis = this.timeInMillis,
        confidencePercent = this.confidencePercent,
        label = this.label,
        readableName = disease?.readableName
    )
}
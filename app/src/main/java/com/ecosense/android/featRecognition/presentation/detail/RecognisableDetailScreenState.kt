package com.ecosense.android.featRecognition.presentation.detail

import com.ecosense.android.featRecognition.domain.model.Disease
import com.ecosense.android.featRecognition.domain.model.RecognisableDetail

data class RecognisableDetailScreenState(
    val recognisableDetail: RecognisableDetail,
    val disease: Disease,
    val isLoadingSaving: Boolean,
    val isLoadingUnsaving: Boolean,
) {
    val isSaved get() = this.recognisableDetail.id != null

    companion object {
        val defaultValue = RecognisableDetailScreenState(
            recognisableDetail = RecognisableDetail(
                id = null,
                savedAt = 0,
                confidencePercent = 0,
                label = "",
            ),
            disease = Disease(
                label = "",
                readableName = null,
                symptoms = null,
                treatments = null,
                preventiveMeasures = null,
            ),
            isLoadingSaving = false,
            isLoadingUnsaving = false,
        )
    }
}
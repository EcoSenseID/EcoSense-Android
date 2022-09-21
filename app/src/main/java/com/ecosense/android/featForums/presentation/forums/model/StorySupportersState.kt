package com.ecosense.android.featForums.presentation.forums.model

import com.ecosense.android.core.util.UIText
import com.ecosense.android.featForums.presentation.model.SupporterPresentation

data class StorySupportersState(
    val supporters: List<SupporterPresentation>,
    val isLoading: Boolean,
    val isEndReached: Boolean,
    val errorMessage: UIText?,
    val page: Int,
) {
    companion object {
        val defaultValue = StorySupportersState(
            supporters = emptyList(),
            isLoading = false,
            isEndReached = false,
            errorMessage = null,
            page = 0,
        )
    }
}
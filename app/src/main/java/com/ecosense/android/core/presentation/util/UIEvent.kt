package com.ecosense.android.core.presentation.util

import com.ecosense.android.core.util.UIText

sealed class UIEvent {
    data class ShowSnackbar(val uiText: UIText) : UIEvent()
    object HideKeyboard : UIEvent()
}
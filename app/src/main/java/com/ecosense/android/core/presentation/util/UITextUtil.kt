package com.ecosense.android.core.presentation.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ecosense.android.core.util.UIText

@Composable
fun UIText.asString(): String {
    return when (this) {
        is UIText.DynamicString -> this.value
        is UIText.StringResource -> stringResource(id = this.id)
    }
}

fun UIText.asString(context: Context): String {
    return when (this) {
        is UIText.DynamicString -> this.value
        is UIText.StringResource -> context.getString(this.id)
    }
}
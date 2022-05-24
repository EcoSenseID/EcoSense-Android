package com.ecosense.android.featAuth.presentation.component

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.ecosense.android.R

@Composable
fun EmailTextField(
    value: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit = {},
    trailingIcon: @Composable (() -> Unit)? = null
) {
    AuthTextField(
        value = value,
        enabled = enabled,
        onValueChange = onValueChange,
        label = { Text(text = stringResource(id = R.string.email)) },
        placeholder = { Text(text = stringResource(id = R.string.email)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        modifier = modifier,
        trailingIcon = trailingIcon
    )
}
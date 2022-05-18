package com.ecosense.android.featAuth.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit),
    placeholder: @Composable (() -> Unit),
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    TextField(
        value = value,
        label = label,
        singleLine = true,
        placeholder = placeholder,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        modifier = modifier.fillMaxWidth(),
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
    )
}
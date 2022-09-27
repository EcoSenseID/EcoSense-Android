package com.ecosense.android.featAuth.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.ecosense.android.core.presentation.theme.DarkGrey
import com.ecosense.android.core.presentation.theme.LightGrey

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit),
    placeholder: @Composable (() -> Unit),
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    TextField(
        value = value,
        label = label,
        enabled = enabled,
        singleLine = true,
        placeholder = placeholder,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = DarkGrey, shape = RoundedCornerShape(16.dp)),
        trailingIcon = trailingIcon,
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            backgroundColor = LightGrey,
        ),
        visualTransformation = visualTransformation,
    )
}
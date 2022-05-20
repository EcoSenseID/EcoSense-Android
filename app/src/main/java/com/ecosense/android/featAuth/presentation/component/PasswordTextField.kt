package com.ecosense.android.featAuth.presentation.component

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.ecosense.android.R

@Composable
fun PasswordTextField(
    value: String,
    isVisible: Boolean,
    onValueChange: (String) -> Unit,
    onChangeVisibility: () -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit = { Text(text = stringResource(id = R.string.password)) },
    placeholder: @Composable () -> Unit = { Text(text = stringResource(id = R.string.password)) }
) {
    AuthTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = modifier,
        trailingIcon = {
            IconButton(onClick = onChangeVisibility) {
                Icon(
                    imageVector = if (isVisible) Icons.Filled.Visibility else
                        Icons.Filled.VisibilityOff,
                    contentDescription = if (isVisible) stringResource(R.string.hide_password) else
                        stringResource(R.string.show_password)
                )
            }
        },
        visualTransformation = if (isVisible) VisualTransformation.None else
            PasswordVisualTransformation(),
    )
}
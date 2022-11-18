package com.ecosense.android.featAuth.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ecosense.android.core.presentation.theme.Gradient

@Composable
fun OutlinedGradientButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 48.dp,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(CornerSize(percent = 100)),
    content: @Composable RowScope.() -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .clip(shape)
            .clickable { if (enabled) onClick() }
            .fillMaxWidth()
            .height(height)
            .background(MaterialTheme.colors.surface)
            .border(width = 1.dp, brush = Gradient, shape = shape),
    ) {
        content.invoke(this)
    }
}
package com.ecosense.android.featForums.presentation.forums.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing

@Composable
fun StorySupportersSection(
    avatarUrls: () -> List<String>,
    totalSupportersCount: () -> Int,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Box(contentAlignment = Alignment.CenterStart) {
            for (i in avatarUrls().indices) {
                Row {
                    Spacer(modifier = Modifier.width((i * 12).dp))
                    AsyncImage(
                        model = avatarUrls()[i],
                        contentDescription = null,
                        placeholder = painterResource(id = R.drawable.ic_ecosense_logo),
                        error = painterResource(id = R.drawable.ic_ecosense_logo),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colors.surface)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colors.surface,
                                shape = CircleShape,
                            ),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

        val restCount = totalSupportersCount() - avatarUrls().size
        if (restCount > 0) Text(
            text = "+$restCount",
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            style = MaterialTheme.typography.caption,
        )
    }
}
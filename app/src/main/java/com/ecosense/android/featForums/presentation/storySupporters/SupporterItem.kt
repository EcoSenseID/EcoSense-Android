package com.ecosense.android.featForums.presentation.storySupporters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.featForums.presentation.model.SupporterPresentation

@Composable
fun SupporterItem(
    supporter: () -> SupporterPresentation,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(MaterialTheme.spacing.medium),
    ) {
        AsyncImage(
            model = supporter().avatarUrl,
            contentDescription = null,
            placeholder = painterResource(id = R.drawable.ic_ecosense_logo),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = supporter().name,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
            )

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

            Text(
                text = supporter().username,
                color = MaterialTheme.colors.onSurface.copy(0.6f),
            )
        }
    }
}